package cn.ussu.modules.ecps.search.service;

import cn.hutool.core.lang.Assert;
import cn.ussu.modules.ecps.item.model.param.SearchParam;
import cn.ussu.modules.ecps.search.model.ItemClobIndex;
import cn.ussu.modules.ecps.search.model.ItemIndex;
import cn.ussu.modules.ecps.search.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

@Service
public class ItemIndexService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private SkuRepository skuRepository;
    @Autowired
    private SpecValueRepository specValueRepository;
    @Autowired
    private SkuImgRepository skuImgRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemClobRepository itemClobRepository;
    @Autowired
    private ItemClobIndexService itemClobIndexService;
    @Autowired
    private ParaValueIndexService paraValueIndexService;

    public Object search(SearchParam searchParam) {
        return null;
    }

    public boolean add(ItemIndex itemIndex) {
        Assert.notNull(itemIndex.getItemId());
        if (!itemRepository.existsById(itemIndex.getItemId())) {
            itemRepository.save(itemIndex);
            ItemClobIndex itemClob = itemIndex.getItemClob();
            if (itemClob != null) {
                itemClobIndexService.add(itemClob);
            }
            paraValueIndexService.addByItemIndex(itemIndex);
        }
        return true;
    }

}
