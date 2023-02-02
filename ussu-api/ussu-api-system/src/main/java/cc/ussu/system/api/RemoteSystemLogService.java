package cc.ussu.system.api;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.system.api.vo.SystemLogVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = ServiceNameConstants.SERVICE_SYSETM)
public interface RemoteSystemLogService {

    @PutMapping("/system-log")
    JsonResult saveLog(@RequestBody SystemLogVO systemLogVO);

}
