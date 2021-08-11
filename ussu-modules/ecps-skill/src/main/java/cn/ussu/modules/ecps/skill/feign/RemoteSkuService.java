package cn.ussu.modules.ecps.skill.feign;

import cn.ussu.modules.ecps.common.constants.ConstantsEcps;
import cn.ussu.modules.ecps.item.entity.EbSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(ConstantsEcps.SERVICE_ECPS_ITEM)
public interface RemoteSkuService {

    @GetMapping("/sku/simpleDetailList/{skuIds}")
    List<EbSku> getSimpleDetailBySkuIdList(@PathVariable("skuIds") String skuIds);
}
