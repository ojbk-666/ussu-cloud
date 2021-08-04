package cn.ussu.modules.ecps.member.feign;

import cn.ussu.common.core.constants.ServiceConstants;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.security.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(ServiceConstants.SERVICE_SYSETM)
public interface RemoteSysUserService {

    @PutMapping("/sys-user")
    JsonResult addSysUser(@RequestBody SysUser sysUser);

}
