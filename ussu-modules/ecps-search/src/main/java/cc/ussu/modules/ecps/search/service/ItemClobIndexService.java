package cc.ussu.modules.ecps.search.service;

import cc.ussu.modules.ecps.search.model.ItemClobIndex;
import cc.ussu.modules.ecps.search.repository.ItemClobRepository;
import cn.hutool.core.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemClobIndexService {

    @Autowired
    private ItemClobRepository itemClobRepository;

    public void add(ItemClobIndex itemClobIndex) {
        Assert.notNull(itemClobIndex.getItemId());
        Integer itemId = itemClobIndex.getItemId();
        if (!itemClobRepository.existsById(itemId)) {
            itemClobRepository.save(itemClobIndex);
        }
    }

}
