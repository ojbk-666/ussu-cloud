package cc.ussu.modules.ecps.member.feign;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.system.api.vo.SysUserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(ServiceNameConstants.SERVICE_SYSETM)
public interface RemoteSysUserService {

    @PutMapping("/sys-user")
    JsonResult addSysUser(@RequestBody SysUserVO sysUser);

}
