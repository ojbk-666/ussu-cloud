package cn.ussu.modules.ecps.search.repository;

import cn.ussu.modules.ecps.search.model.BrandIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends ElasticsearchRepository<BrandIndex, Integer> {

}
