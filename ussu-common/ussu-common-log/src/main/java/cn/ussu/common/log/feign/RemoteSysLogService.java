package cn.ussu.common.log.feign;

import cn.ussu.common.core.constants.ServiceConstants;
import cn.ussu.common.core.constants.StrConstants;
import cn.ussu.common.log.model.vo.LogVo;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@EnableFeignClients
@FeignClient(contextId = "remoteSysLogService", name = ServiceConstants.SERVICE_SYSETM)
public interface RemoteSysLogService {

    @PutMapping("/sys-log" + StrConstants.SLASH_remote)
    void recordLog(@RequestBody LogVo logVo);

}
