package cn.ussu.modules.ecps.portal.feign.item;

import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.ecps.common.constants.ConstantsEcps;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(ConstantsEcps.SERVICE_ECPS_ITEM)
public interface RemoteBrandService {

    @GetMapping("/brand/catId/{catId}")
    JsonResult getBrandlistByCatId(@PathVariable("catId") Integer catId);

}
