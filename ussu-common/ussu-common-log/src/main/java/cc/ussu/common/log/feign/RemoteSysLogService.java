package cc.ussu.common.log.feign;

import cc.ussu.common.log.model.vo.LogVo;
import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.constants.StrConstants;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@EnableFeignClients
@FeignClient(contextId = "remoteSysLogService", name = ServiceNameConstants.SERVICE_SYSETM)
public interface RemoteSysLogService {

    @PutMapping("/sys-log" + StrConstants.SLASH_remote)
    void recordLog(@RequestBody LogVo logVo);

}
