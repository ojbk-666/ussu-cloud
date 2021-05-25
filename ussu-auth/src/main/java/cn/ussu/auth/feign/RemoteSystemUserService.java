package cn.ussu.auth.feign;

import cn.ussu.common.core.constants.ServiceConstants;
import cn.ussu.common.core.entity.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 远程调用用户服务模块
 */
@FeignClient(name = ServiceConstants.SERVICE_SYSETM)
public interface RemoteSystemUserService {

    @GetMapping("/sys-user/account")
    JsonResult getUserByUsername(@RequestParam("username") String username);

    @PostMapping("/abc")
    void updateUserLastLoginInfo();

    @PostMapping("/sys-user/insertOrUpdateByThirdAlipay")
    JsonResult insertOrUpdateByThirdAlipay(@RequestParam Map param);

}
