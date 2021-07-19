package cn.ussu.modules.ecps.item.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.ecps.item.entity.EbItem;
import cn.ussu.modules.ecps.item.entity.EbItemClob;
import cn.ussu.modules.ecps.item.entity.EbParaValue;
import cn.ussu.modules.ecps.item.entity.EbSku;
import cn.ussu.modules.ecps.item.mapper.EbItemMapper;
import cn.ussu.modules.ecps.item.service.IEbItemService;
import cn.ussu.modules.ecps.item.service.IEbParaValueService;
import cn.ussu.modules.ecps.item.service.IEbSkuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品，包含手机和号卡，通过在类目表中预置的手机类目和号卡类目来区分。裸机：手机机体，不包含任何通信服务和绑定的费用的机器 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
@Service
public class EbItemServiceImpl extends ServiceImpl<EbItemMapper, EbItem> implements IEbItemService {

    @Autowired
    private IEbParaValueService paraValueService;
    @Autowired
    private IEbSkuService skuService;

    @Override
    public EbItem detail(Integer id) {
        Assert.notNull(id);
        EbItem obj = new EbItem().selectById(id);
        obj.setItemClob(new EbItemClob().setItemId(obj.getItemId()).selectById());
        return obj;
    }

    @Transactional
    @Override
    public void add(EbItem p) {
        Assert.notBlank(p.getItemName(), "名称不能为空");
        Assert.notNull(p.getCatId(), "商品分类不能为空");
        Date now = new Date();
        // 添加商品
        p.setAuditStatus(0)
                .setCreateTime(now)
                .setCreateUserId(SecurityUtils.getUserId())
                .setSales(0)
                .setItemNo(IdWorker.getTimeId())
                .setLastStatus(1)
                .insert();
        // 添加商品详情
        EbItemClob itemClob = p.getItemClob();
        itemClob.setItemId(p.getItemId()).insert();
        // 添加属性
        List<EbParaValue> paraList = p.getParaList();
        for (EbParaValue para : paraList) {
            para.setItemId(p.getItemId());
        }
        paraValueService.saveBatch(paraList);
        // 添加sku
        List<EbSku> skuList = p.getSkuList();
        for (EbSku sku : skuList) {
            sku.setItemId(p.getItemId())
                    // 202107181402015241416639374887501825
                    .setSku(IdWorker.getTimeId())
                    .setCreateTime(now)
                    .setCreateUserId(SecurityUtils.getUserId())
                    .setLastStatus(1)
                    .setSkuType(1)
                    .setSales(0);
            skuService.add(sku);
        }
    }

    @Transactional
    @Override
    public void edit(EbItem p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbItem> qw = new LambdaQueryWrapper<>();
        qw.in(EbItem::getItemId, ids);
        super.remove(qw);
    }

}
