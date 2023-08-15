package cc.ussu.modules.log.es.mapper;

import cc.ussu.modules.log.es.domain.LogEntity;
import cn.easyes.core.conditions.interfaces.BaseEsMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface EsLogMapper extends BaseEsMapper<LogEntity> {
}
