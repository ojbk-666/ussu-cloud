package cn.ussu.modules.ecps.search.repository;

import cn.ussu.modules.ecps.search.model.SpecValueIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecValueRepository extends ElasticsearchRepository<SpecValueIndex, Integer> {

    List<SpecValueIndex> getAllBySkuIdEquals(Integer skuId);

}
