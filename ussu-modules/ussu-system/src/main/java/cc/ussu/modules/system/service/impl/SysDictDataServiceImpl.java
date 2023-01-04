package cc.ussu.modules.system.service.impl;

import cc.ussu.common.core.constants.StrConstants;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.common.redis.util.DictUtil;
import cc.ussu.modules.system.entity.SysDictData;
import cc.ussu.modules.system.entity.SysDictType;
import cc.ussu.modules.system.mapper.SysDictDataMapper;
import cc.ussu.modules.system.service.ISysDictDataService;
import cc.ussu.modules.system.service.ISysDictTypeService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-12-31 20:31:02
 */
@Master
@Service
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {

    @Autowired
    private ISysDictTypeService sysDictTypeService;
    @Autowired
    private RedisService redisService;

    /**
     * 是否存在类型，应该存在，类型必须存在
     */
    @Override
    public boolean checkTypeExist(SysDictData p) {
        LambdaQueryWrapper<SysDictType> qw = Wrappers.lambdaQuery(SysDictType.class).eq(SysDictType::getDictType, p.getDictType());
        return sysDictTypeService.count(qw) > 0;
    }

    /**
     * 是否存在值，应该否 值不允许重复
     */
    @Override
    public boolean checkValueExist(SysDictData p) {
        LambdaQueryWrapper<SysDictData> qw = Wrappers.lambdaQuery(SysDictData.class)
                .eq(SysDictData::getDictValue, p.getDictValue())
                .eq(SysDictData::getDictType, p.getDictType())
                .ne(StrUtil.isNotBlank(p.getId()), SysDictData::getId, p.getId());
        return super.count(qw) > 0;
    }

    /**
     * 检查是否存在编码
     *
     * @param p
     */
    @Override
    public boolean checkCodeExist(SysDictData p) {
        LambdaQueryWrapper<SysDictData> qw = Wrappers.lambdaQuery(SysDictData.class)
                .eq(SysDictData::getDictCode, p.getDictCode())
                .eq(SysDictData::getDictType, p.getDictType())
                .ne(StrUtil.isNotBlank(p.getId()), SysDictData::getId, p.getId());
        return super.count(qw) > 0;
    }

    /**
     * 根据类型获取状态为正常的数据列表
     *
     * @param type
     */
    @Override
    public List<SysDictData> getListByDictTypeNotDisabled(String type) {
        if (StrUtil.isBlank(type)) {
            return new ArrayList<>();
        }
        return super.list(Wrappers.lambdaQuery(SysDictData.class)
                .orderByAsc(SysDictData::getCreateTime)
                .eq(SysDictData::getDictType, type)
                .eq(SysDictData::getDisableFlag, StrConstants.CHAR_FALSE));
    }

    /**
     * 刷新缓存
     */
    @Override
    public synchronized void refreshCache() {
        // 清空
        Set<String> keys = redisService.redisTemplate.keys(DictUtil.KEY_PREFIX + "*");
        redisService.deleteObject(keys);
        // 重新写入
        List<SysDictType> typeList = sysDictTypeService.list(Wrappers.lambdaQuery(SysDictType.class)
                .eq(SysDictType::getDisableFlag, StrConstants.CHAR_FALSE).isNotNull(SysDictType::getDictType));
        if (CollUtil.isNotEmpty(typeList)) {
            List<SysDictData> dataList = super.list(Wrappers.lambdaQuery(SysDictData.class)
                    .eq(SysDictData::getDisableFlag, StrConstants.CHAR_FALSE)
                    .in(SysDictData::getDictType, typeList.stream().map(SysDictType::getDictType).collect(Collectors.toList())));
            for (SysDictType type : typeList) {
                String key = DictUtil.KEY_PREFIX + type.getDictType();
                for (SysDictData data : dataList) {
                    if (type.getDictType().equals(data.getDictType())) {
                        redisService.setCacheMapValue(key, data.getDictCode(), data.getDictValue());
                    }
                }
            }
        }
    }

}
