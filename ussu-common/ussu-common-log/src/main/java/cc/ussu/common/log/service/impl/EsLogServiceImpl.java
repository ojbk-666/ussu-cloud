package cc.ussu.common.log.service.impl;

import cc.ussu.common.log.service.LogService;
import cc.ussu.log.api.RemoteLogService;
import cc.ussu.log.api.vo.LogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnMissingBean(LogService.class)
public class EsLogServiceImpl implements LogService {

    @Autowired
    private RemoteLogService remoteLogService;

    @Override
    public void saveLog(LogVO logVO) {
        remoteLogService.saveLog(logVO);
    }
}
