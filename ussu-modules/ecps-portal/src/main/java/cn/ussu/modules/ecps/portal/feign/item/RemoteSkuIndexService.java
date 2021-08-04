package cn.ussu.modules.ecps.portal.feign.item;

import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.ecps.common.constants.ConstantsEcps;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(ConstantsEcps.SERVICE_ECPS_SEARCH)
public interface RemoteSkuIndexService {

    @GetMapping("/search/sku")
    JsonResult searchSkuForPage(@RequestParam("catId") Integer searchParamCatId,
                                @RequestParam("brandId") Integer brandId,
                                @RequestParam("price") String price,
                                @RequestParam("paras") String paras,
                                @RequestParam("keywords") String keywords,
                                @RequestParam("hasStore") Integer hasStore,
                                @RequestParam("sort") String sort,
                                @RequestParam("pageNum") Integer pageNum);

    @GetMapping("/search/sku/detail/{skuId}")
    JsonResult detail(@PathVariable("skuId") Integer skuId);
}
