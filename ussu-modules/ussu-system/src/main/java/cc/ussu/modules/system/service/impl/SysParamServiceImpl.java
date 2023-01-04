package cc.ussu.modules.system.service.impl;

import cc.ussu.common.core.constants.StrConstants;
import cc.ussu.modules.system.entity.SysParam;
import cc.ussu.modules.system.mapper.SysParamMapper;
import cc.ussu.modules.system.properties.UssuProperties;
import cc.ussu.modules.system.service.ISysParamService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统参数表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2021-12-17 14:53:58
 */
@Master
@Service
public class SysParamServiceImpl extends ServiceImpl<SysParamMapper, SysParam> implements ISysParamService {

    @Autowired
    private UssuProperties ussuProperties;

    /**
     * 检查是否已存在name
     *
     * @param p
     * @return
     */
    @Override
    public boolean checkNameExist(SysParam p) {
        LambdaQueryWrapper<SysParam> qw = Wrappers.lambdaQuery(SysParam.class)
                .eq(SysParam::getParamName, p.getParamName())
                .ne(StrUtil.isNotBlank(p.getId()), SysParam::getId, p.getId());
        long count = super.count(qw);
        return count > 0;
    }

    /**
     * 检查是否已存在key
     *
     * @param p
     * @return
     */
    @Override
    public boolean checkKeyExist(SysParam p) {
        LambdaQueryWrapper<SysParam> qw = Wrappers.lambdaQuery(SysParam.class)
                .eq(SysParam::getParamKey, p.getParamKey())
                .ne(StrUtil.isNotBlank(p.getId()), SysParam::getId, p.getId());
        long count = super.count(qw);
        return count > 0;
    }

    @Override
    public SysParam getByKey(String key) {
        return super.getOne(Wrappers.lambdaQuery(SysParam.class).eq(SysParam::getParamKey, key));
    }

    @Override
    public boolean getCaptchaEnable() {
        SysParam v = getByKey("ussu:captchaEnable");
        if (v != null && StrConstants.CHAR_FALSE.equals(v.getDisableFlag())) {
            return "true".equals(v.getParamValue());
        }
        return ussuProperties.getCaptchaEnable();
    }

    @Override
    public String getValueByKey(String key) {
        SysParam byKey = getByKey(key);
        if (byKey != null && StrConstants.CHAR_FALSE.equals(byKey.getDisableFlag())) {
            return byKey.getParamValue();
        }
        return null;
    }

    @Override
    public String getValueByKey(String key, String defaultValue) {
        return StrUtil.blankToDefault(getValueByKey(key), defaultValue);
    }

    @Override
    public boolean getValueByKeyBoolean(String key) {
        return "true".equals(getValueByKey(key, "false"));
    }
}
