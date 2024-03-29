package cc.ussu.modules.ecps.item.feign;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.modules.ecps.common.constants.ConstantsEcps;
import cc.ussu.modules.ecps.item.entity.EbSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(ConstantsEcps.SERVICE_ECPS_SEARCH)
public interface RemoteSearchService {

    /**
     * 上架商品同步至es库
     */
    @PutMapping("/search/sku")
    JsonResult up(@RequestBody EbSku sku);


}
