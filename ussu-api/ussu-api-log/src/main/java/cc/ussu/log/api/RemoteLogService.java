package cc.ussu.log.api;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.log.api.vo.LogVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = ServiceNameConstants.SERVICE_LOG)
public interface RemoteLogService {

    @PutMapping("/log")
    JsonResult saveLog(@RequestBody LogVO logVO);

}
