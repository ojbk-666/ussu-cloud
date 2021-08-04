package cn.ussu.modules.ecps.search.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.model.vo.SimplePageInfo;
import cn.ussu.common.core.model.vo.SimplePageResult;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.modules.ecps.item.entity.EbItem;
import cn.ussu.modules.ecps.item.model.param.SearchParam;
import cn.ussu.modules.ecps.search.model.*;
import cn.ussu.modules.ecps.search.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkuIndexService {

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
    private ItemIndexService itemIndexService;
    @Autowired
    private SpecValueIndexService specValueIndexService;
    @Autowired
    private SkuImgIndexService skuImgIndexService;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private BrandIndexService brandIndexService;
    @Autowired
    private RedisService redisService;

    /**
     * 分页
     */
    public SimplePageResult<SkuIndex> findPage(SearchParam searchParam) {
        Integer catId = searchParam.getCatId();
        Integer brandId = searchParam.getBrandId();
        Integer pageNum = searchParam.getPageNum();
        if (pageNum == null || pageNum < 0) {
            pageNum = 1;
        }
        Integer realPageNum = pageNum - 1;
        Integer pageSize = 20;
        String price = searchParam.getPrice();
        int[] priceSplit = StrUtil.splitToInt(price, StrPool.DASHED);
        System.out.println(searchParam);
        if (catId != null) {
            List<ItemIndex> itemList = itemRepository.getAllByCatIdEquals(catId);
            List<Integer> itemIdList = itemList.stream().map(ItemIndex::getItemId).collect(Collectors.toList());
            SearchPage<SkuIndex> pageResult = skuRepository.findAllByItemIdIn(itemIdList, PageRequest.of(realPageNum, pageSize));
            long total = pageResult.getTotalElements();
            SimplePageInfo simplePageInfo = new SimplePageInfo().setTotal(total).setCurrent(pageNum).setSize(pageSize);
            List<SkuIndex> resultList = pageResult.getContent().stream().map(SearchHit::getContent).collect(Collectors.toList());
            for (SkuIndex skuIndex : resultList) {
                ItemIndex itemIndex = itemRepository.getByItemIdEquals(skuIndex.getItemId());
                skuIndex.setBrand(brandRepository.findById(itemIndex.getBrandId()).orElse(new BrandIndex()));
            }
            SimplePageResult<SkuIndex> simplePageResult = new SimplePageResult<SkuIndex>()
                    .setPageInfo(simplePageInfo)
                    .setRecords(resultList);
            return simplePageResult;
        }
        return null;
    }

    /**
     * 获取SKU详情
     */
    public SkuIndex detial(Integer skuId) {
        Assert.notNull(skuId, "skuId不能为空");
        SkuIndex skuIndex = skuRepository.findById(skuId).get();
        // SkuIndex skuIndex = skuRepository.getFirstBySkuEquals(sku);
        Assert.notNull(skuIndex, "商品不存在");
        // 商品
        ItemIndex itemIndex = itemRepository.getByItemIdEquals(skuIndex.getItemId());
        if (itemIndex != null) {
            // 商品详情
            ItemClobIndex itemClobIndex = itemClobRepository.getByItemIdEquals(itemIndex.getItemId());
            String itemDesc = itemClobIndex.getItemDesc();
            if (StrUtil.isNotBlank(itemDesc)) {
                itemDesc = itemDesc.replaceAll(StrPool.LF, StrUtil.EMPTY).replaceAll(StrPool.CR, StrUtil.EMPTY)
                        .replaceAll(StrPool.CRLF, StrUtil.EMPTY).replaceAll(StrPool.TAB, StrUtil.EMPTY);
                itemClobIndex.setItemDesc(itemDesc);
            }
            itemIndex.setItemClob(itemClobIndex);
            BrandIndex brandIndex = brandRepository.findById(itemIndex.getBrandId()).get();
            itemIndex.setBrand(brandIndex);
            skuIndex.setItem(itemIndex);
        }
        // 规格
        List<SpecValueIndex> specValueIndexList = specValueRepository.getAllBySkuIdEquals(skuIndex.getSkuId());
        skuIndex.setSpecList(specValueIndexList);
        // 图片
        List<SkuImgIndex> skuImgIndexList = skuImgRepository.getAllBySkuIdEquals(skuIndex.getSkuId());
        skuIndex.setSkuImgList(skuImgIndexList);
        return skuIndex;
    }

    /**
     * sku写入es
     */
    public void addSkuIndex(SkuIndex skuIndex) {
        EbItem ebItem = redisService.getCacheObject("up_sku_skuid_" + skuIndex.getSkuId());
        // EbBrand ebBrand = ebItem.getBrand();
        // EbItemClob ebItemClob = ebItem.getItemClob();
        // List<EbParaValue> ebParaValueList = ebItem.getParaList();
        ItemIndex itemIndex = BeanUtil.toBean(ebItem, ItemIndex.class);
        // BrandIndex brandIndex = BeanUtil.toBean(ebBrand, BrandIndex.class);
        // ItemClobIndex itemClobIndex = BeanUtil.toBean(ebItemClob, ItemClobIndex.class);
        // itemIndex.setBrand(brandIndex).setItemClob(itemClobIndex);
        skuIndex.setItem(itemIndex);
        skuRepository.save(skuIndex);
        itemIndexService.add(skuIndex.getItem());
        brandIndexService.add(skuIndex.getItem().getBrand());
        specValueIndexService.addBySku(skuIndex);
        skuImgIndexService.addBySku(skuIndex);
    }

}
