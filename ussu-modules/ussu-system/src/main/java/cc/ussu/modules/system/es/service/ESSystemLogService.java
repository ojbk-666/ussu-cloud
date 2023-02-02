package cc.ussu.modules.system.es.service;

import cc.ussu.modules.system.es.domain.SystemLog;
import cc.ussu.system.api.vo.SystemLogVO;
import cn.easyes.core.biz.EsPageInfo;

public interface ESSystemLogService {

    EsPageInfo<SystemLog> findPage(SystemLog query);

    void saveLog(SystemLogVO systemLogVO);

}
