package cn.ussu.auth.feign;

import cn.ussu.auth.model.param.login.ThirdLoginToSysUserParam;
import cn.ussu.common.core.constants.ServiceConstants;
import cn.ussu.common.core.model.vo.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Deprecated
    @PostMapping("/sys-user/insertOrUpdateByThirdAlipay")
    JsonResult insertOrUpdateByThirdAlipay(@RequestParam Map param);

    @PostMapping("/sys-user/insertOrUpdateByThird")
    Object insertOrUpdateByThird(@RequestBody ThirdLoginToSysUserParam param);

}
