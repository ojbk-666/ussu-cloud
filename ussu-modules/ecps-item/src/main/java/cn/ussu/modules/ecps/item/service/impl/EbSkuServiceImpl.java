package cn.ussu.modules.ecps.item.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.ecps.item.entity.EbSku;
import cn.ussu.modules.ecps.item.entity.EbSkuImg;
import cn.ussu.modules.ecps.item.entity.EbSpecValue;
import cn.ussu.modules.ecps.item.mapper.EbSkuMapper;
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

import java.util.Date;
import java.util.List;

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

    @Override
    public EbSku detail(Integer id) {
        Assert.notNull(id);
        EbSku obj = new EbSku().selectById(id);
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
        List<EbSkuImg> skuImgList = p.getSkuImgList();
        for (EbSkuImg skuImg : skuImgList) {
            skuImg.setSkuId(p.getSkuId());
            if (skuImg.getDefaultImg() == null) {
                skuImg.setDefaultImg(0);
            }
            if (skuImg.getDefaultImg().equals(1)) {
                existDefaultImage = true;
            }
        }
        if (!existDefaultImage) {
            if (CollUtil.isNotEmpty(skuImgList)) {
                skuImgList.get(0).setDefaultImg(1);
            }
        }
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
        qw.in(EbSku::getSkuId, ids);
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
}
