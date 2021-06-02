package cn.ussu.modules.system.service;

import cn.ussu.common.datasource.model.vo.ReturnPageInfo;
import cn.ussu.modules.system.entity.SysLog;
import cn.ussu.modules.system.model.param.SysLogParam;
import cn.ussu.modules.system.model.result.SysLogResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author liming
 * @since 2020-05-06
 */
public interface ISysLogService extends IService<SysLog> {

    ReturnPageInfo<SysLogResult> findPage(SysLogParam param);

    /**
     * 分页查询结果
     */
    List<Map<String, Object>> findCountGroupBy(String log_name);
}
