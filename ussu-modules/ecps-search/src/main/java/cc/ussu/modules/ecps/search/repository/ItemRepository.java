package cc.ussu.modules.ecps.search.repository;

import cc.ussu.modules.ecps.search.model.ItemIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends ElasticsearchRepository<ItemIndex, Integer> {

    ItemIndex getByItemIdEquals(Integer itemId);

    List<ItemIndex> getAllByCatIdEquals(Integer catId);

    List<ItemIndex> getAllByCatIdEqualsAndBrandIdEquals(Integer catId, Integer brandId);

}
