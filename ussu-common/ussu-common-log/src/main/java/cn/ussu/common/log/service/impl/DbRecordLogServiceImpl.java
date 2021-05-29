package cn.ussu.common.log.service.impl;

import cn.ussu.common.log.feign.RemoteSysLogService;
import cn.ussu.common.log.model.vo.LogVo;
import cn.ussu.common.log.service.RecordLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class DbRecordLogServiceImpl implements RecordLogService {

    @Autowired
    private RemoteSysLogService remoteSysLogService;

    @Async
    @Override
    public void recordLog(LogVo logVo) {
        remoteSysLogService.recordLog(logVo);
    }

}
