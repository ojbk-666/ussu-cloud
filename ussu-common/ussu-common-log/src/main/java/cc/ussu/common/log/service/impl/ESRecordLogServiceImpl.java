package cc.ussu.common.log.service.impl;

import cc.ussu.common.log.service.RecordLogService;
import cc.ussu.system.api.RemoteSystemLogService;
import cc.ussu.system.api.vo.SystemLogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

@Deprecated
@Slf4j
@Service
@ConditionalOnMissingBean(RecordLogService.class)
public class ESRecordLogServiceImpl implements RecordLogService {

    @Autowired
    private RemoteSystemLogService remoteSystemLogService;

    @Override
    public void saveLog(SystemLogVO systemLogVO) {
        try {
            remoteSystemLogService.saveLog(systemLogVO);
        } catch (Exception ex) {
            log.warn("日志记录失败：{}，{}，{}", systemLogVO.getServiceName(), systemLogVO.getGroup(), systemLogVO.getName());
        }
    }
}
