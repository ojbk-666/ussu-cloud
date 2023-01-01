package cc.ussu.modules.ecps.search.repository;

import cc.ussu.modules.ecps.search.model.ParaValueIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParaValueRepository extends ElasticsearchRepository<ParaValueIndex, Integer> {
}
