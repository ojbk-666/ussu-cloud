package cn.ussu.modules.ecps.item.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.modules.ecps.item.entity.EbSpecValue;
import cn.ussu.modules.ecps.item.mapper.EbSpecValueMapper;
import cn.ussu.modules.ecps.item.service.IEbSpecValueService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 规格值（与价格有关） 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-07-17
 */
@Service
public class EbSpecValueServiceImpl extends ServiceImpl<EbSpecValueMapper, EbSpecValue> implements IEbSpecValueService {

    @Override
    public EbSpecValue detail(Integer id) {
        Assert.notNull(id);
        EbSpecValue obj = new EbSpecValue().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbSpecValue p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbSpecValue p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbSpecValue> qw = new LambdaQueryWrapper<>();
        qw.in(EbSpecValue::getSpecId, ints);
        super.remove(qw);
    }

    @Override
    public List<EbSpecValue> getBySkuId(Integer skuId) {
        Assert.notNull(skuId);
        LambdaQueryWrapper<EbSpecValue> qw = Wrappers.lambdaQuery(EbSpecValue.class)
                .orderByAsc(EbSpecValue::getSpecId)
                .eq(EbSpecValue::getSkuId, skuId);
        return super.list(qw);
    }

}
