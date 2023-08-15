package cc.ussu.common.log.service;

import cc.ussu.system.api.vo.SystemLogVO;

@Deprecated
public interface RecordLogService {

    void saveLog(SystemLogVO systemLogVO);

}
