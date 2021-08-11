package cn.ussu.modules.ecps.portal.feign.member;

import cn.ussu.modules.ecps.common.constants.ConstantsEcps;
import cn.ussu.modules.ecps.member.entity.EbCartSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(ConstantsEcps.SERVICE_ECPS_MEMBER)
public interface RemoteCartService {

    @GetMapping("/cart/user")
    List<EbCartSku> getCartListByUserId();

}
