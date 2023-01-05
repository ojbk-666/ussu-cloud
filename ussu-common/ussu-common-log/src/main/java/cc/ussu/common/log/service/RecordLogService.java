package cc.ussu.common.log.service;

import cc.ussu.system.api.vo.SystemLogVO;

public interface RecordLogService {

    void saveLog(SystemLogVO systemLogVO);

}
