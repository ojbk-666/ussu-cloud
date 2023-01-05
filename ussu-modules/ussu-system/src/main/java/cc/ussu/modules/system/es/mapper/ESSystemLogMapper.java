package cc.ussu.modules.system.es.mapper;

import cc.ussu.modules.system.es.domain.SystemLog;
import cn.easyes.core.conditions.interfaces.BaseEsMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface ESSystemLogMapper extends BaseEsMapper<SystemLog> {
}
