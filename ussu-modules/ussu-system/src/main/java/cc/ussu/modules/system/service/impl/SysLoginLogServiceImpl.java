package cc.ussu.modules.system.service.impl;

import cc.ussu.modules.system.entity.SysLoginLog;
import cc.ussu.modules.system.mapper.SysLoginLogMapper;
import cc.ussu.modules.system.service.ISysLoginLogService;
import com.baomidou.dynamic.datasource.annotation.Master;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户会话记录/登录日志 表 服务实现类
 * </p>
 *
 * @author liming
 * @since 2022-01-01 17:31:29
 */
@Master
@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {

}
