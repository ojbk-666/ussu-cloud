package cn.ussu.modules.ecps.member.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.ecps.item.entity.EbItem;
import cn.ussu.modules.ecps.item.entity.EbSku;
import cn.ussu.modules.ecps.item.entity.EbSpecValue;
import cn.ussu.modules.ecps.member.entity.EbCartSku;
import cn.ussu.modules.ecps.member.feign.RemoteSkuService;
import cn.ussu.modules.ecps.member.mapper.EbCartSkuMapper;
import cn.ussu.modules.ecps.member.service.IEbCartSkuService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-07-30
 */
@Service
public class EbCartSkuServiceImpl extends ServiceImpl<EbCartSkuMapper, EbCartSku> implements IEbCartSkuService {

    @Autowired
    private RemoteSkuService remoteSkuService;

    @Override
    public EbCartSku detail(Integer id) {
        Assert.notNull(id);
        EbCartSku obj = new EbCartSku().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbCartSku p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbCartSku p) {
        p.updateById();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbCartSku> qw = new LambdaQueryWrapper<>();
        qw.in(EbCartSku::getCartSkuId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

    /**
     * 商品加入购物车
     */
    @Override
    public synchronized boolean addSkuToCartBySkuId(Integer skuId) {
        Assert.notNull(skuId);
        // 检查是否已有
        LambdaQueryWrapper<EbCartSku> qw = Wrappers.lambdaQuery(EbCartSku.class)
                .eq(EbCartSku::getEbUserId, SecurityUtils.getUserId()).eq(EbCartSku::getSkuId, skuId);
        EbCartSku one = super.getOne(qw);
        if (one != null) {
            new EbCartSku().setCartSkuId(one.getCartSkuId())
                    .setQuantity(one.getQuantity() + 1)
                    .setTotalPrice(NumberUtil.mul(one.getPrice(), one.getQuantity() + 1))
                    .updateById();
        } else {
            JsonResult jr = remoteSkuService.getSkuBySkuId(skuId);
            EbSku sku = jr.getData(EbSku.class);
            EbItem item = sku.getItem();
            Map<String, Object> skuSpec = new HashMap<>();
            for (EbSpecValue spec : sku.getSpecList()) {
                skuSpec.put(spec.getFeatureName(), spec.getSpecValue());
            }
            new EbCartSku().setEbUserId(SecurityUtils.getUserId())
                    .setItemId(item.getItemId())
                    .setItemNo(item.getItemNo())
                    .setItemName(item.getItemName())
                    .setSkuId(sku.getSkuId())
                    .setSku(sku.getSku())
                    .setSkuName(sku.getSkuName())
                    .setSkuImg(sku.getSkuImg())
                    .setSkuSpec(JSON.toJSONString(skuSpec))
                    .setMarketPrice(sku.getMarketPrice())
                    .setPrice(sku.getSkuPrice())
                    .setQuantity(1)
                    .setTotalPrice(NumberUtil.mul(sku.getSkuPrice(), 1))
                    .insert();
        }
        return true;
    }
}
