package cc.ussu.modules.ecps.portal.feign.item;

import cc.ussu.modules.ecps.common.constants.ConstantsEcps;
import cc.ussu.modules.ecps.item.entity.EbParaValue;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(ConstantsEcps.SERVICE_ECPS_ITEM)
public interface RemoteParaValueService {

    @GetMapping("/para-value/item/{itemId}")
    List<EbParaValue> allByItemId(@PathVariable("itemId") Integer itemId);

}
