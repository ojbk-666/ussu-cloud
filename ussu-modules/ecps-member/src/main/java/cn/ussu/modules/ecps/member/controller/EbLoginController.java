package cn.ussu.modules.ecps.member.controller;

import cn.hutool.core.lang.Assert;
import cn.ussu.common.core.base.BaseController;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.security.entity.SysUser;
import cn.ussu.modules.ecps.member.entity.EbUser;
import cn.ussu.modules.ecps.member.feign.RemoteSysUserService;
import cn.ussu.modules.ecps.member.service.IEbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class EbLoginController extends BaseController {

    @Autowired
    private IEbUserService ebUserServicel;
    @Autowired
    private RemoteSysUserService remoteSysUserService;

    /**
     * 注册
     */
    @PostMapping("/register")
    public JsonResult register(String username, String password) {
        // check
        Assert.notBlank(username);
        List<String> roleIds = new ArrayList<>();
        SysUser sysUser = new SysUser().setAccount(username)
                .setName(username)
                .setNickName(username)
                .setDeptId("1000")
                .setRoleIds(roleIds)
                .setSex(1)
                .setUserType(2);
        JsonResult jsonResult = remoteSysUserService.addSysUser(sysUser);
        if (jsonResult.isSuccess()) {
            new EbUser().setUsername(sysUser.getName())
                    .setPassword(password)
                    .setGender(1)
                    .setRegisterTime(new Date())
                    .insert();
            return JsonResult.ok();
        } else {
            return jsonResult;
        }
    }

}
