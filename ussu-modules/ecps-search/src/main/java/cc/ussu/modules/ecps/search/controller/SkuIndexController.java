package cc.ussu.modules.ecps.search.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.modules.ecps.item.model.param.SearchParam;
import cc.ussu.modules.ecps.search.model.SkuIndex;
import cc.ussu.modules.ecps.search.repository.SkuRepository;
import cc.ussu.modules.ecps.search.service.SkuIndexService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search/sku")
public class SkuIndexController extends BaseSearchController {

    @Autowired
    private SkuIndexService skuIndexService;
    @Autowired
    private SkuRepository skuRepository;

    @GetMapping
    public JsonResult page(SearchParam searchParam) {
        return JsonResult.ok(skuIndexService.findPage(searchParam));
    }

    /**
     * 获取sku详情
     */
    @GetMapping("/detail/{skuId}")
    public SkuIndex detail(@PathVariable Integer skuId) {
        return skuIndexService.detial(skuId);
    }

    /**
     * 商品上架
     */
    @PutMapping
    public JsonResult up(@RequestBody SkuIndex skuIndex) {
        System.out.println(StrUtil.format("SKU上架同步至ElasticSearch skuId:{}, sku:{}, name:{}", skuIndex.getSkuId(), skuIndex.getSku(), skuIndex.getSkuName()));
        skuIndexService.addSkuIndex(skuIndex);
        return JsonResult.ok();
    }

    /**
     * 更新sku销量
     */
    @PostMapping("/sales")
    public JsonResult updateSkuSales(SkuIndex sku) {
        SkuIndex skuIndex = skuRepository.findById(sku.getSkuId()).orElse(null);
        if (skuIndex != null) {
            System.out.println(skuIndex);
            System.out.println(sku);
            // 合并新属性
            BeanUtil.copyProperties(sku, skuIndex, CopyOptions.create(Object.class, true));
            System.out.println(skuIndex);
            // 保存
            skuRepository.save(skuIndex);
        }
        return JsonResult.ok();
    }

}
