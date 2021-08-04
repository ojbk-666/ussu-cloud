package cn.ussu.modules.ecps.search.repository;

import cn.ussu.modules.ecps.search.model.SkuIndex;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuRepository extends ElasticsearchRepository<SkuIndex, Integer> {

    SkuIndex getFirstBySkuEquals(String sku);

    SearchPage<SkuIndex> findAllByItemIdIn(List<Integer> catIds, Pageable pageable);

}
