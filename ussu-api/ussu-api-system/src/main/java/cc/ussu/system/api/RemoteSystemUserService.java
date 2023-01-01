package cc.ussu.system.api;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 远程调用用户服务模块
 */
@FeignClient(value = ServiceNameConstants.SERVICE_SYSETM, contextId = "remoteSystemUserService")
public interface RemoteSystemUserService {

    @GetMapping("/sys-user/get-by-account/{account}")
    JsonResult<LoginUser> getUserByUsername(@PathVariable("account") String account);

    @PostMapping("/sys-user/insertOrUpdateByThird")
    JsonResult<LoginUser> insertOrUpdateByThird(@RequestBody Map param);

}
