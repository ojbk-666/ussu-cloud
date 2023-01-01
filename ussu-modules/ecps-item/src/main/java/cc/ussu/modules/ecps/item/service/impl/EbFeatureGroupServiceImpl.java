package cc.ussu.modules.ecps.item.service.impl;

import cc.ussu.modules.ecps.item.entity.EbFeature;
import cc.ussu.modules.ecps.item.entity.EbFeatureGroup;
import cc.ussu.modules.ecps.item.mapper.EbFeatureGroupMapper;
import cc.ussu.modules.ecps.item.mapper.EbFeatureMapper;
import cc.ussu.modules.ecps.item.service.IEbFeatureGroupService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-07-16
 */
@Service
public class EbFeatureGroupServiceImpl extends ServiceImpl<EbFeatureGroupMapper, EbFeatureGroup> implements IEbFeatureGroupService {

    @Autowired
    private EbFeatureMapper featureMapper;

    @Override
    public EbFeatureGroup detail(Integer id) {
        Assert.notNull(id);
        EbFeatureGroup obj = new EbFeatureGroup().selectById(id);
        return obj;
    }

    @Transactional
    @Override
    public void add(EbFeatureGroup p) {
        p.insert();
    }

    @Transactional
    @Override
    public void edit(EbFeatureGroup p) {
        p.updateById();
    }

    @Transactional
    @Override
    public void del(String ids) {
        int[] ints = StrUtil.splitToInt(ids, StrPool.COMMA);
        Assert.isTrue(ints.length > 0);
        LambdaQueryWrapper<EbFeatureGroup> qw = new LambdaQueryWrapper<>();
        qw.in(EbFeatureGroup::getGroupId, Arrays.stream(ints).boxed().collect(Collectors.toList()));
        super.remove(qw);
    }

    @Transactional
    @Override
    public void link(EbFeatureGroup p) {
        if (p.getCatId() == null) {
            EbFeatureGroup ebFeatureGroup = new EbFeatureGroup().setGroupId(p.getGroupId()).selectById();
            p.setCatId(ebFeatureGroup.getCatId());
        }
        // 先删除
        featureMapper.updateGroupIdNullByGroupIdAndCatId(p.getGroupId(), p.getCatId());
        // 后关联
        List<Integer> featureIdList = p.getFeatureIdList();
        if (CollUtil.isNotEmpty(featureIdList)) {
            LambdaQueryWrapper<EbFeature> qw = new LambdaQueryWrapper<>();
            qw.eq(EbFeature::getCatId, p.getCatId())
                    .in(EbFeature::getFeatureId, featureIdList);
            featureMapper.update(new EbFeature().setGroupId(p.getGroupId()), qw);
        }
    }
}
