package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.entity.DcInterfaceLog;
import cc.ussu.modules.dczx.mapper.DcInterfaceLogMapper;
import cc.ussu.modules.dczx.service.IDcInterfaceLogService;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 接口日志 服务实现类
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Slave
@Service
public class DcInterfaceLogServiceImpl extends ServiceImpl<DcInterfaceLogMapper, DcInterfaceLog> implements IDcInterfaceLogService {

}
