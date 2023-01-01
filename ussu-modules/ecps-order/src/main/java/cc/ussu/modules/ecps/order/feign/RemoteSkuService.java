package cc.ussu.modules.ecps.order.feign;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.modules.ecps.common.constants.ConstantsEcps;
import cc.ussu.modules.ecps.item.entity.EbSku;
import cc.ussu.modules.ecps.order.entity.EbOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(ConstantsEcps.SERVICE_ECPS_ITEM)
public interface RemoteSkuService {

    @GetMapping("/sku/simpleDetail/{skuId}")
    EbSku getSimpleDetail(@PathVariable("skuId") Integer skuId);

    @PostMapping("/sku/updateStock")
    JsonResult updateStock(@RequestParam("skuId") Integer skuId, @RequestParam("stockInventory") Integer stockInventory);

    @PostMapping("/sku/rollbackStock")
    JsonResult rollbackStock(@RequestBody EbOrder order);

}
