package cc.ussu.modules.ecps.member.feign;

import cc.ussu.modules.ecps.common.constants.ConstantsEcps;
import cc.ussu.modules.ecps.item.entity.EbSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(ConstantsEcps.SERVICE_ECPS_ITEM)
public interface RemoteSkuService {

    @GetMapping("/sku/detail2/{skuId}")
    EbSku getSkuBySkuId(@PathVariable("skuId") Integer skuId);

}
