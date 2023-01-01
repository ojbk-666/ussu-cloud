package cc.ussu.modules.ecps.portal.feign.order;

import cc.ussu.modules.ecps.common.constants.ConstantsEcps;
import cc.ussu.modules.ecps.order.entity.EbOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(ConstantsEcps.SERVICE_ECPS_ORDER)
public interface RemoteOrderService {

    @PutMapping("/order/submitOrder")
    EbOrder submitOrder(@RequestBody EbOrder order);

}
