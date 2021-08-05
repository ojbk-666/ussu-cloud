package cn.ussu.modules.ecps.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.ecps.item.entity.EbSku;
import cn.ussu.modules.ecps.member.entity.EbCartSku;
import cn.ussu.modules.ecps.order.entity.EbOrder;
import cn.ussu.modules.ecps.order.entity.EbOrderDetail;
import cn.ussu.modules.ecps.order.feign.RemoteCartService;
import cn.ussu.modules.ecps.order.feign.RemoteSkuService;
import cn.ussu.modules.ecps.order.mapper.EbOrderMapper;
import cn.ussu.modules.ecps.order.service.IEbOrderDetailService;
import cn.ussu.modules.ecps.order.service.IEbOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单。包括实体商品和虚拟商品的订单 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-08-03
 */
@Service
public class EbOrderServiceImpl extends ServiceImpl<EbOrderMapper, EbOrder> implements IEbOrderService {

    @Autowired
    private IEbOrderDetailService orderDetailService;
    @Autowired
    private RemoteCartService remoteCartService;
    @Autowired
    private RemoteSkuService remoteSkuService;
    @Autowired
    private RedissonClient redisson;

    private RLock lock;

    @Autowired
    public void setLock() {
        this.lock = redisson.getLock("order_sku_store_lock");
    }

    @Override
    public EbOrder detail(Integer id) {
        Assert.notNull(id);
        EbOrder obj = new EbOrder().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbOrder p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbOrder p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbOrder> qw = new LambdaQueryWrapper<>();
        qw.in(EbOrder::getOrderId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

    /**
     * 提交订单
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional
    @Override
    public EbOrder submitOrder(EbOrder order, String cartIds, String userId) {
        int[] cartIdArr = StrUtil.splitToInt(cartIds, StrPool.COMMA);
        // 购物车idList
        List<Integer> cartIdList = Arrays.stream(cartIdArr).boxed().collect(Collectors.toList());
        JsonResult allCartJr = remoteCartService.getCartListByUserId();
        List<Map> data = allCartJr.getData();
        List<EbCartSku> cartList = filterSelectedCartList(BeanUtil.copyToList(data, EbCartSku.class), cartIdList);
        BigDecimal orderSum = new BigDecimal("0");
        BigDecimal shipFee = new BigDecimal("0");
        for (EbCartSku cartSku : cartList) {
            orderSum = NumberUtil.add(orderSum, cartSku.getTotalPrice());
        }
        // 写入订单表
        order.setEbUserId(userId)
                .setUsername(SecurityUtils.getLoginUserSysUser().getName())
                .setOrderNum(IdWorker.getIdStr())
                .setDelivery(2)
                .setIsConfirm(0)
                .setShipFee(new BigDecimal("0"))
                .setOrderSum(NumberUtil.add(orderSum, shipFee))
                .setIsPaid(0)
                .setOrderState(1)
                .setOrderTime(new Date())
                .setIsDelete(0)
                .setIsDisplay(0)
                .setOrderTime(new Date());
        baseMapper.insert(order);
        System.out.println("1.写入订单");
        // 写入订单详情表
        List<EbOrderDetail> orderDetailList = new LinkedList<>();
        for (EbCartSku cart : cartList) {
            EbOrderDetail orderDetail = BeanUtil.toBean(cart, EbOrderDetail.class)
                    .setOrderId(order.getOrderId())
                    .setSkuPrice(cart.getPrice())
                    .setPrice(cart.getTotalPrice())
                    .setPaymentPrice(cart.getTotalPrice());
            orderDetailList.add(orderDetail);
        }
        orderDetailService.saveBatch(orderDetailList);
        System.out.println("2.写入订单详情");
        order.setOrderDetailList(orderDetailList);
        // 校验扣减库存
        for (EbOrderDetail orderDetail : orderDetailList) {
            Integer skuId = orderDetail.getSkuId();
            try {
                lock.lock();
                JsonResult simpleDetailJr = remoteSkuService.getSimpleDetail(skuId);
                EbSku sku = simpleDetailJr.getData(EbSku.class);
                // 校验库存
                int stock = sku.getStockInventory() - orderDetail.getQuantity().intValue();
                if (stock < 0) {
                    throw new RuntimeException(StrUtil.format("商品【{}】库存不足", orderDetail.getSkuName()));
                }
                // 更新库存
                remoteSkuService.updateStock(skuId, stock);
                System.out.println("3.更新库存："+skuId);
            } finally {
                lock.unlock();
            }
        }
        // 清空购物车
        remoteCartService.deleteByIds(cartIds);
        System.out.println("4.清空购物车");
        return order;
    }

    /**
     * 从购物车列表筛选已选择的
     *
     * @param list
     * @param selectedIds
     * @return
     */
    private List<EbCartSku> filterSelectedCartList(List<EbCartSku> list, List<Integer> selectedIds) {
        LinkedList<EbCartSku> r = new LinkedList<>();
        for (EbCartSku cartSku : list) {
            if (selectedIds.contains(cartSku.getCartSkuId())) {
                r.push(cartSku);
            }
        }
        return r;
    }

}
