package cc.ussu.modules.ecps.portal.feign.item;

import cc.ussu.modules.ecps.common.constants.ConstantsEcps;
import cc.ussu.modules.ecps.item.entity.EbCat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = ConstantsEcps.SERVICE_ECPS_ITEM)
public interface RemoteItemService {

    @GetMapping("/eb-cat/list/tree")
    List<EbCat> listCatTree();

}
