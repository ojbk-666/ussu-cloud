package cn.ussu.modules.ecps.search.service;

import cn.hutool.core.lang.Assert;
import cn.ussu.modules.ecps.search.model.BrandIndex;
import cn.ussu.modules.ecps.search.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

@Service
public class BrandIndexService {

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
    @Autowired
    private BrandRepository brandRepository;

    public boolean add(BrandIndex brandIndex) {
        Assert.notNull(brandIndex.getBrandId());
        if (!brandRepository.existsById(brandIndex.getBrandId())) {
            brandRepository.save(brandIndex);
        }
        return true;
    }

}
