package cn.ussu.modules.ecps.portal.feign.item;

import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.ecps.common.constants.ConstantsEcps;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = ConstantsEcps.SERVICE_ECPS_ITEM)
public interface RemoteItemService {

    @GetMapping("/eb-cat/list/tree")
    // List<EbCat> listCatTree();
    JsonResult listCatTree();

}
