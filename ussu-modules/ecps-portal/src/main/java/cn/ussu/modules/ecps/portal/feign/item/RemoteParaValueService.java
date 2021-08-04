package cn.ussu.modules.ecps.portal.feign.item;

import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.ecps.common.constants.ConstantsEcps;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(ConstantsEcps.SERVICE_ECPS_ITEM)
public interface RemoteParaValueService {

    @GetMapping("/para-value/item/{itemId}")
    JsonResult allByItemId(@PathVariable("itemId") Integer itemId);

}
