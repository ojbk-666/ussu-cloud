package cc.ussu.modules.ecps.order.service.impl;

import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.ecps.common.constants.ConstantsEcpsRabbit;
import cc.ussu.modules.ecps.common.constants.OrderPayStatus;
import cc.ussu.modules.ecps.common.constants.OrderStatus;
import cc.ussu.modules.ecps.item.entity.EbSku;
import cc.ussu.modules.ecps.member.entity.EbCartSku;
import cc.ussu.modules.ecps.order.config.EcpsRabbitTemplate;
import cc.ussu.modules.ecps.order.entity.EbOrder;
import cc.ussu.modules.ecps.order.entity.EbOrderDetail;
import cc.ussu.modules.ecps.order.feign.RemoteCartService;
import cc.ussu.modules.ecps.order.feign.RemoteSkuService;
import cc.ussu.modules.ecps.order.mapper.EbOrderMapper;
import cc.ussu.modules.ecps.order.service.IEbOrderDetailService;
import cc.ussu.modules.ecps.order.service.IEbOrderService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
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
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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

    @Autowired
    private EcpsRabbitTemplate ecpsRabbitTemplate;

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
        List<EbCartSku> cartListByUserId = remoteCartService.getCartListByUserId();
        List<EbCartSku> cartList = filterSelectedCartList(cartListByUserId, cartIdList);
        BigDecimal orderSum = new BigDecimal("0");
        BigDecimal shipFee = new BigDecimal("0");
        for (EbCartSku cartSku : cartList) {
            orderSum = NumberUtil.add(orderSum, cartSku.getTotalPrice());
        }
        // 写入订单表
        order.setEbUserId(userId)
                .setUsername(SecurityUtil.getLoginUserSysUser().getAccount())
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
                EbSku sku = remoteSkuService.getSimpleDetail(skuId);
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
        ecpsRabbitTemplate.convertAndSend(ConstantsEcpsRabbit.EXCHANGE_ORDER_DEAD, ConstantsEcpsRabbit.ROUTING_KEY_ORDER_DEAD, order);
        return order;
    }

    /**
     * 关闭订单
     */
    @GlobalTransactional
    @Transactional
    @Override
    public boolean closeOrder(EbOrder order) {
        Assert.notNull(order.getOrderId());
        // 获取最新状态
        EbOrder newOrder = new EbOrder().setOrderId(order.getOrderId()).selectById();
        if (OrderPayStatus.NOT_PAID.getCode().equals(newOrder.getIsPaid())) {
            // 未支付则关闭订单并回滚库存
            new EbOrder().setOrderId(newOrder.getOrderId())
                    .setIsPaid(OrderPayStatus.CLOSE.getCode())
                    .setOrderState(OrderStatus.CLOSE.getCode())
                    .updateById();
            remoteSkuService.rollbackStock(order);
        }
        return true;
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
