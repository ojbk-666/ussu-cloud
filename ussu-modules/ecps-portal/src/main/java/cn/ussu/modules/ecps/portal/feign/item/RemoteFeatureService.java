package cn.ussu.modules.ecps.portal.feign.item;

import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.ecps.common.constants.ConstantsEcps;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(ConstantsEcps.SERVICE_ECPS_ITEM)
public interface RemoteFeatureService {

    @GetMapping("/feature/catId/{catId}")
    JsonResult getFeaturelistByCatId(@PathVariable("catId") Integer catId,
                                     @RequestParam("isSpec") Integer isSpec,
                                     @RequestParam("isSelect") Integer isSelect);

}