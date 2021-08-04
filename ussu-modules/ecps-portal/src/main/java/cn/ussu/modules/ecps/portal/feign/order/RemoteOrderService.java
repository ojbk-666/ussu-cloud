package cn.ussu.modules.ecps.portal.feign.order;

import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.modules.ecps.common.constants.ConstantsEcps;
import cn.ussu.modules.ecps.order.entity.EbOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(ConstantsEcps.SERVICE_ECPS_ORDER)
public interface RemoteOrderService {

    @PutMapping("/order/submitOrder")
    JsonResult submitOrder(@RequestBody EbOrder order);

}
