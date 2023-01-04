package cc.ussu.modules.system.mapper;

import cc.ussu.modules.system.entity.SysLoginLog;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户会话记录/登录日志 表 Mapper 接口
 * </p>
 *
 * @author liming
 * @since 2022-01-01 17:31:29
 */
@Master
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

}
