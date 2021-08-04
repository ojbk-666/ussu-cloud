package cn.ussu.modules.ecps.search.repository;

import cn.ussu.modules.ecps.search.model.SkuImgIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuImgRepository extends ElasticsearchRepository<SkuImgIndex, Integer> {

    List<SkuImgIndex> getAllBySkuIdEquals(Integer skuId);

}
