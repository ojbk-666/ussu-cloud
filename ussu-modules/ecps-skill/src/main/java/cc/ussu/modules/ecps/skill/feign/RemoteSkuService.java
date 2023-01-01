package cc.ussu.modules.ecps.skill.feign;

import cc.ussu.modules.ecps.common.constants.ConstantsEcps;
import cc.ussu.modules.ecps.item.entity.EbSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = ConstantsEcps.SERVICE_ECPS_ITEM, fallback = RemoteSkuServiceFallback.class)
public interface RemoteSkuService {

    @GetMapping("/sku/simpleDetailList/{skuIds}")
    List<EbSku> getSimpleDetailBySkuIdList(@PathVariable("skuIds") String skuIds);
}
