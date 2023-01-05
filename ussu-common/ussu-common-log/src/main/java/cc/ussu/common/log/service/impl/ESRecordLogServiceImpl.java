package cc.ussu.common.log.service.impl;

import cc.ussu.common.log.service.RecordLogService;
import cc.ussu.system.api.RemoteSystemLogService;
import cc.ussu.system.api.vo.SystemLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ESRecordLogServiceImpl implements RecordLogService {

    @Autowired
    private RemoteSystemLogService remoteSystemLogService;

    @Override
    public void saveLog(SystemLogVO systemLogVO) {
        remoteSystemLogService.saveLog(systemLogVO);
    }
}
