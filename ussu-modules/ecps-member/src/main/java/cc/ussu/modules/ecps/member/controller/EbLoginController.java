package cc.ussu.modules.ecps.member.controller;

import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.modules.ecps.member.entity.EbUser;
import cc.ussu.modules.ecps.member.feign.RemoteSysUserService;
import cc.ussu.modules.ecps.member.service.IEbUserService;
import cc.ussu.system.api.vo.SysUserVO;
import cn.hutool.core.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
        Set<String> roleIds = new HashSet<>();
        SysUserVO userVO = new SysUserVO().setAccount(username)
                .setAccount(username)
                .setNickName(username)
                .setDeptId("1000")
                .setRoleIdList(roleIds)
                .setSex("1");
        JsonResult jsonResult = remoteSysUserService.addSysUser(userVO);
        if (jsonResult.isOk()) {
            new EbUser().setUsername(userVO.getAccount())
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
