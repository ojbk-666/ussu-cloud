package cn.ussu.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.common.datasource.util.DefaultPageFactory;
import cn.ussu.modules.system.entity.SysParam;
import cn.ussu.modules.system.mapper.SysParamMapper;
import cn.ussu.modules.system.model.param.SysParamParam;
import cn.ussu.modules.system.service.ISysParamService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统参数表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
@Service
public class SysParamServiceImpl extends ServiceImpl<SysParamMapper, SysParam> implements ISysParamService {

    @Autowired
    private SysParamMapper sysParamMapper;

    @Override
    public ReturnPageInfo<SysParam> findPage(SysParamParam param) {
        LambdaQueryWrapper<SysParam> qw = new LambdaQueryWrapper<>();
        qw.orderByDesc(SysParam::getCreateTime);
        if (param != null) {
            qw.like(StrUtil.isNotBlank(param.getParamName()), SysParam::getParamName, param.getParamName())
                    .like(StrUtil.isNotBlank(param.getParamKey()), SysParam::getParamKey, param.getParamKey())
                    .eq(param.getModule() != null, SysParam::getModule, param.getModule());
        }
        IPage<SysParam> iPage = sysParamMapper.selectPage(DefaultPageFactory.getPage(), qw);
        return DefaultPageFactory.createReturnPageInfo(iPage);
    }
    
}
