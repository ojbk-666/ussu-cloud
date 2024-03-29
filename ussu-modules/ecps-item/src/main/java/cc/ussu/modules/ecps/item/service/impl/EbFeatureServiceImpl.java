package cc.ussu.modules.ecps.item.service.impl;

import cc.ussu.modules.ecps.common.constants.ConstantsEcps;
import cc.ussu.modules.ecps.item.entity.EbFeature;
import cc.ussu.modules.ecps.item.mapper.EbFeatureMapper;
import cc.ussu.modules.ecps.item.service.IEbFeatureService;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品属性	预置的手机参数（请将预置可选值补充完整）	1.      型号             服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-07-16
 */
@Service
public class EbFeatureServiceImpl extends ServiceImpl<EbFeatureMapper, EbFeature> implements IEbFeatureService {

    @Override
    public EbFeature detail(Integer id) {
        Assert.notNull(id);
        EbFeature obj = new EbFeature().selectById(id);
        return obj;
    }

    @CacheEvict(value = ConstantsEcps.CACHE_VALUE_FEATURE, allEntries = true)
    @Transactional
    @Override
    public void add(EbFeature p) {
        p.insert();
    }

    @CacheEvict(value = ConstantsEcps.CACHE_VALUE_FEATURE, key = "'getFeatureListByCatId_' + #root.args[0].catId")
    @Transactional
    @Override
    public void edit(EbFeature p) {
        p.updateById();
    }

    @CacheEvict(value = ConstantsEcps.CACHE_VALUE_FEATURE, allEntries = true)
    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbFeature> qw = new LambdaQueryWrapper<>();
        qw.in(EbFeature::getFeatureId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

}
