package cc.ussu.common.log.service.impl;

import cc.ussu.common.log.model.vo.LogVo;
import cc.ussu.common.log.service.RecordLogService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class DbRecordLogServiceImpl implements RecordLogService {

    // @Autowired
    // private RemoteSysLogService remoteSysLogService;

    @Async
    @Override
    public void recordLog(LogVo logVo) {
        // remoteSysLogService.recordLog(logVo);
    }

}
