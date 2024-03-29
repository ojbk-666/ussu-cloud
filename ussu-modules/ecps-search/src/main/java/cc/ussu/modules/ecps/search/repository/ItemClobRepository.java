package cc.ussu.modules.ecps.search.repository;

import cc.ussu.modules.ecps.search.model.ItemClobIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemClobRepository extends ElasticsearchRepository<ItemClobIndex, Integer> {

    ItemClobIndex getByItemIdEquals(Integer itemId);

}
