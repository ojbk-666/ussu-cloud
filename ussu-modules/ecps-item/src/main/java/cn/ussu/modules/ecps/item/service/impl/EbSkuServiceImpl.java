package cn.ussu.modules.ecps.item.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.ecps.item.entity.*;
import cn.ussu.modules.ecps.item.feign.RemoteSearchService;
import cn.ussu.modules.ecps.item.mapper.EbSkuMapper;
import cn.ussu.modules.ecps.item.service.IEbItemService;
import cn.ussu.modules.ecps.item.service.IEbSkuImgService;
import cn.ussu.modules.ecps.item.service.IEbSkuService;
import cn.ussu.modules.ecps.item.service.IEbSpecValueService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 最小销售单元，包括实体商品、虚拟商品（如号卡、套卡、话费等）	将要增加的字段：	STOCK_IN 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
@Service
public class EbSkuServiceImpl extends ServiceImpl<EbSkuMapper, EbSku> implements IEbSkuService {

    @Autowired
    private IEbSpecValueService specValueService;
    @Autowired
    private IEbSkuImgService skuImgService;
    @Autowired
    private IEbItemService itemService;
    @Autowired
    private RemoteSearchService remoteSearchService;
    @Autowired
    private RedisService redisService;

    @Override
    public EbSku detail(Integer id, boolean includeItem) {
        Assert.notNull(id);
        EbSku obj = new EbSku().selectById(id);
        obj.setSpecList(specValueService.getBySkuId(obj.getSkuId()))
                .setSkuImgList(skuImgService.getBySkuId(obj.getSkuId()));
        if (includeItem) {
            EbItem item = itemService.detail(obj.getItemId(), false);
            EbBrand brand = new EbBrand().setBrandId(item.getBrandId()).selectById();
            item.setBrand(brand);
            obj.setItem(item);
        }
        return obj;
    }

    @Override
    public EbSku detail2(Integer skuId, boolean includeItem) {
        Assert.notNull(skuId);
        EbSku obj = new EbSku().selectById(skuId);
        obj.setSpecList(specValueService.getBySkuId(obj.getSkuId()))
                .setSkuImgList(skuImgService.getBySkuId(obj.getSkuId()));
        for (EbSpecValue specValue : obj.getSpecList()) {
            Integer featureId = specValue.getFeatureId();
            EbFeature ebFeature = new EbFeature().setFeatureId(featureId).selectById();
            specValue.setFeatureName(ebFeature.getFeatureName());
        }
        if (includeItem) {
            EbItem ebItem = new EbItem().setItemId(obj.getItemId()).selectById();
            obj.setItem(ebItem);
        }
        return obj;
    }

    @Transactional
    @Override
    public void add(EbSku p) {
        Assert.notBlank(p.getSkuName());
        Assert.notNull(p.getItemId());
        if (p.getCreateTime() == null) {
            p.setCreateTime(new Date());
        }
        if (StrUtil.isBlank(p.getCreateUserId())) {
            p.setCreateUserId(SecurityUtils.getUserId());
        }
        if (StrUtil.isBlank(p.getSku())) {
            p.setSku(IdWorker.getIdStr());
        }
        // 写入sku
        p.insert();
        // 写入规格列表
        List<EbSpecValue> specList = p.getSpecList();
        for (EbSpecValue spec : specList) {
            spec.setSkuId(p.getSkuId());
        }
        specValueService.saveBatch(specList);
        // 写入图片列表
        boolean existDefaultImage = false;
        String skuImgOfSku = null;
        List<EbSkuImg> skuImgList = p.getSkuImgList();
        for (EbSkuImg skuImg : skuImgList) {
            skuImg.setSkuId(p.getSkuId());
            if (skuImg.getDefaultImg() == null) {
                skuImg.setDefaultImg(0);
            }
            if (skuImg.getDefaultImg().equals(1)) {
                existDefaultImage = true;
                skuImgOfSku = skuImg.getImgUrl();
            }
        }
        if (!existDefaultImage) {
            if (CollUtil.isNotEmpty(skuImgList)) {
                skuImgList.get(0).setDefaultImg(1);
                skuImgOfSku = skuImgList.get(0).getImgUrl();
            }
        }
        new EbSku().setSkuId(p.getSkuId()).setSkuImg(skuImgOfSku).updateById();
        skuImgService.saveBatch(skuImgList);
    }

    @Transactional
    @Override
    public void edit(EbSku p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbSku> qw = new LambdaQueryWrapper<>();
        qw.in(EbSku::getSkuId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

    @Override
    public List<EbSku> getByItemId(Integer itemId) {
        Assert.notNull(itemId);
        LambdaQueryWrapper<EbSku> qw = Wrappers.lambdaQuery(EbSku.class)
                .orderByAsc(EbSku::getSkuSort, EbSku::getSkuId)
                .eq(EbSku::getItemId, itemId);
        List<EbSku> list = super.list(qw);
        for (EbSku sku : list) {
            // 设置规格
            sku.setSpecList(specValueService.getBySkuId(sku.getSkuId()));
            // 设置图片
            sku.setSkuImgList(skuImgService.getBySkuId(sku.getSkuId()));
        }
        return list;
    }

    @Transactional
    @Override
    public void upSkuBySkuId(String idstr) {
        int[] ids = StrUtil.splitToInt(idstr, StrPool.COMMA);
        List<Integer> skuIds = Arrays.stream(ids).boxed().collect(Collectors.toList());
        Assert.notEmpty(skuIds);
        for (Integer skuId : skuIds) {
            EbSku ebSku = new EbSku().setSkuId(skuId).selectById();
            if (ebSku.getShowStatus().equals(0)) {
                new EbSku().setSkuId(skuId).setShowStatus(1).updateById();
                EbSku sku = detail(skuId, true);
                redisService.setCacheObject("up_sku_skuid_" + sku.getSkuId(), sku.getItem(), 30L);
                sku.setItem(null);
                JsonResult jsonResult = remoteSearchService.up(sku);
                if (!jsonResult.isSuccess()) {
                    throw new IllegalArgumentException(jsonResult.getMsg());
                }
            }
        }
    }

    /**
     * 更新商品库存
     *
     * @param sku
     */
    @Transactional
    @Override
    public void updateStock(EbSku sku) {
        Assert.notNull(sku.getSkuId(), "skuId不能为空");
        Assert.notNull(sku.getStockInventory(), "库存不能为空");
        new EbSku().setSkuId(sku.getSkuId())
                .setStockInventory(sku.getStockInventory())
                .updateById();
    }
}
