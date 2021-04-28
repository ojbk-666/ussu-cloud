package cn.ussu.auth.feign;

import cn.ussu.common.core.constants.ServiceConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用用户服务模块
 */
@FeignClient(name = ServiceConstants.SERVICE_SYSETM)
public interface RemoteSystemUserService {

    @GetMapping
    void getUserByUsername(@RequestParam String username);

}
