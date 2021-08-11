package cn.ussu.modules.ecps.order.feign;

import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.ecps.common.constants.ConstantsEcps;
import cn.ussu.modules.ecps.member.entity.EbCartSku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(ConstantsEcps.SERVICE_ECPS_MEMBER)
public interface RemoteCartService {

    @GetMapping("/cart/user")
    List<EbCartSku> getCartListByUserId();

    @DeleteMapping("/cart/{ids}")
    JsonResult deleteByIds(@PathVariable("ids") String ids);

}
