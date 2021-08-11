package cn.ussu.modules.ecps.portal.feign.item;

import cn.ussu.modules.ecps.common.constants.ConstantsEcps;
import cn.ussu.modules.ecps.item.entity.EbFeature;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(ConstantsEcps.SERVICE_ECPS_ITEM)
public interface RemoteFeatureService {

    @GetMapping("/feature/catId/{catId}")
    List<EbFeature> getFeaturelistByCatId(@PathVariable("catId") Integer catId,
                                          @RequestParam("isSpec") Integer isSpec,
                                          @RequestParam("isSelect") Integer isSelect);

}
