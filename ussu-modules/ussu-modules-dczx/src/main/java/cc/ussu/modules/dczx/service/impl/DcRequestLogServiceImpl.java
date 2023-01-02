package cc.ussu.modules.dczx.service.impl;

import cc.ussu.modules.dczx.entity.DcRequestLog;
import cc.ussu.modules.dczx.mapper.DcRequestLogMapper;
import cc.ussu.modules.dczx.service.IDcRequestLogService;
import com.baomidou.dynamic.datasource.annotation.Slave;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 东财接口访问日志 服务实现类
 * </p>
 *
 * @author mp-generator
 * @since 2022-06-03 14:16:34
 */
@Slave
@Service
public class DcRequestLogServiceImpl extends ServiceImpl<DcRequestLogMapper, DcRequestLog> implements IDcRequestLogService {

}
