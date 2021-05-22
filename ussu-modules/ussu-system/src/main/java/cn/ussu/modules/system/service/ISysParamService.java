package cn.ussu.modules.system.service;

import cn.ussu.common.core.entity.ReturnPageInfo;
import cn.ussu.modules.system.entity.SysParam;
import cn.ussu.modules.system.model.param.SysParamParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统参数表 服务类
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
public interface ISysParamService extends IService<SysParam> {

    ReturnPageInfo<SysParam> findPage(SysParamParam param);

}
