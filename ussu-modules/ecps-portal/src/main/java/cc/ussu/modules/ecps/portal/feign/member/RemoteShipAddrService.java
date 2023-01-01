package cc.ussu.modules.ecps.portal.feign.member;

import cc.ussu.modules.ecps.common.constants.ConstantsEcps;
import cc.ussu.modules.ecps.member.entity.EbShipAddr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(ConstantsEcps.SERVICE_ECPS_MEMBER)
public interface RemoteShipAddrService {

    @GetMapping("/ship-addr/allByUserId/{userId}")
    List<EbShipAddr> allByUserId(@PathVariable("userId") String userId);

}
