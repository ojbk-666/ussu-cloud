package cn.ussu.modules.ecps.portal.feign.member;

import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.ecps.common.constants.ConstantsEcps;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(ConstantsEcps.SERVICE_ECPS_MEMBER)
public interface RemoteShipAddrService {

    @GetMapping("/ship-addr/allByUserId/{userId}")
    JsonResult allByUserId(@PathVariable("userId") String userId);

}
