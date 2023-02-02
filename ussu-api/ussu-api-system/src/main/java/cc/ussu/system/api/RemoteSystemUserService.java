package cc.ussu.system.api;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 远程调用用户服务模块
 */
@FeignClient(value = ServiceNameConstants.SERVICE_SYSETM, contextId = "remoteSystemUserService")
public interface RemoteSystemUserService {

    @GetMapping("/sys-user/get-by-account/{account}")
    JsonResult<LoginUser> getUserByUsername(@PathVariable("account") String account);

    @GetMapping("/sys-user/get-by-id/{userId}")
    JsonResult<Map<String, Object>> getUserById(@PathVariable("userId") String userId);

    @GetMapping("/sys-user/get-by-ext")
    JsonResult<LoginUser> getUserByExt1(@RequestParam("ext1") String ext1, @RequestParam("detail") boolean detail);

    @GetMapping("/sys-user/get-by-ext")
    JsonResult<LoginUser> getUserByExt2(@RequestParam("ext2") String ext2, @RequestParam("detail") boolean detail);

    @GetMapping("/sys-user/get-by-ext")
    JsonResult<LoginUser> getUserByExt3(@RequestParam("ext3")String ext3, @RequestParam("detail") boolean detail);

    @PostMapping("/user/profile/ext")
    JsonResult updateUserExt(@RequestBody Map<String, Object> map);

}
