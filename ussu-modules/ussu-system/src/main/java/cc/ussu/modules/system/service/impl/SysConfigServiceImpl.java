package cc.ussu.modules.system.service.impl;

import cc.ussu.common.core.constants.StrConstants;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.common.redis.util.ConfigUtil;
import cc.ussu.modules.system.entity.SysConfig;
import cc.ussu.modules.system.entity.vo.SysConfigGroupVO;
import cc.ussu.modules.system.entity.vo.SysConfigVO;
import cc.ussu.modules.system.mapper.SysConfigMapper;
import cc.ussu.modules.system.service.ISysConfigService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author mp-generator
 * @since 2023-03-01 14:45:52
 */
@Master
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    @Autowired
    private RedisService redisService;

    @Override
    public void refreshCache() {
        String lockKey = "ussu:lock:system:sys-config";
        try {
            if (redisService.setNx(lockKey, 1, 60)) {
                // 删除
                Set<String> keys = redisService.getRedisTemplate().keys(ConfigUtil.KEY_PREFIX + "*");
                redisService.deleteObject(keys);
                // 新增
                List<SysConfig> list = list(Wrappers.lambdaQuery(SysConfig.class).eq(SysConfig::getDisableFlag, StrConstants.CHAR_FALSE)
                        .eq(SysConfig::getGroupFlag, StrConstants.CHAR_FALSE));
                for (SysConfig sysConfig : list) {
                    String key = ConfigUtil.KEY_PREFIX + sysConfig.getGroupCode();
                    redisService.setCacheMapValue(key, sysConfig.getCode(), sysConfig.getValue());
                }
            }
        } finally {
            redisService.deleteObject(lockKey);
        }
    }

    @Override
    public SysConfigGroupVO getByGroupCode(String groupCode) {
        SysConfig sysConfig = getOne(Wrappers.lambdaQuery(SysConfig.class).eq(SysConfig::getGroupFlag, StrConstants.CHAR_TRUE).eq(SysConfig::getGroupCode, groupCode));
        return BeanUtil.toBean(sysConfig, SysConfigGroupVO.class);
    }

    /**
     * 检查分组编码是否存在
     */
    @Override
    public boolean existGroupByGroupCode(String groupCode, String id) {
        return super.count(Wrappers.lambdaQuery(SysConfig.class).eq(SysConfig::getGroupFlag, StrConstants.CHAR_TRUE)
                .eq(SysConfig::getGroupCode, groupCode).ne(StrUtil.isNotBlank(id), SysConfig::getId, id)) > 0;
    }

    /**
     * 检查分组下编码是否存在
     */
    @Override
    public boolean existDataByGroupCodeAndCode(String groupCode, String code, String id) {
        return super.count(Wrappers.lambdaQuery(SysConfig.class).eq(SysConfig::getGroupFlag, StrConstants.CHAR_FALSE)
                .eq(SysConfig::getGroupCode, groupCode).eq(SysConfig::getCode, code)
                .ne(StrUtil.isNotBlank(id), SysConfig::getId, id)) > 0;
    }

    @Transactional
    @Override
    public void addGroup(SysConfigGroupVO vo) {
        SysConfig sysConfig = BeanUtil.toBean(vo, SysConfig.class);
        Assert.isFalse(existGroupByGroupCode(sysConfig.getGroupCode(), null), "分组编码已存在");
        sysConfig.setGroupFlag(StrConstants.CHAR_TRUE).setDisableFlag(StrConstants.CHAR_FALSE);
        super.save(sysConfig);
    }

    @Transactional
    @Override
    public void editGroup(SysConfigGroupVO vo) {
        Assert.notBlank(vo.getId(), "id不能为空");
        SysConfig sysConfig = BeanUtil.toBean(vo, SysConfig.class);
        Assert.isFalse(existGroupByGroupCode(sysConfig.getGroupCode(), sysConfig.getId()), "分组编码已存在");
        sysConfig.setGroupFlag(StrConstants.CHAR_TRUE);
        super.updateById(sysConfig);
    }

    @Transactional
    @Override
    public void addData(SysConfigVO vo) {
        SysConfig sysConfig = BeanUtil.toBean(vo, SysConfig.class);
        String groupCode = vo.getGroupCode();
        SysConfigGroupVO group = getByGroupCode(groupCode);
        Assert.isFalse(existDataByGroupCodeAndCode(sysConfig.getGroupCode(), sysConfig.getCode(), null), "编码已存在");
        sysConfig.setGroupFlag(StrConstants.CHAR_FALSE).setDisableFlag(StrConstants.CHAR_FALSE).setGroupName(group.getGroupName());
        super.save(sysConfig);
    }

    @Transactional
    @Override
    public void editData(SysConfigVO vo) {
        Assert.notBlank(vo.getId(), "id不能为空");
        SysConfig sysConfig = BeanUtil.toBean(vo, SysConfig.class);
        SysConfigGroupVO group = getByGroupCode(sysConfig.getGroupCode());
        Assert.isFalse(existDataByGroupCodeAndCode(sysConfig.getGroupCode(), sysConfig.getCode(), sysConfig.getId()), "编码已存在");
        sysConfig.setGroupFlag(StrConstants.CHAR_FALSE).setGroupName(group.getGroupName());
        super.updateById(sysConfig);
    }
}
