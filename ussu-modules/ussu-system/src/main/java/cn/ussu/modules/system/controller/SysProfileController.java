package cn.ussu.modules.system.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.digest.MD5;
import cn.ussu.common.core.base.BaseAdminController;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.core.entity.LocalFileVo;
import cn.ussu.common.log.annotation.InsertLog;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.modules.system.entity.SysMenu;
import cn.ussu.modules.system.entity.SysUser;
import cn.ussu.modules.system.feign.RemoteFileService;
import cn.ussu.modules.system.model.param.SysUserParam;
import cn.ussu.modules.system.model.vo.RouterVo;
import cn.ussu.modules.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 当前用户信息相关
 */
@RestController
@RequestMapping("/profile")
public class SysProfileController extends BaseAdminController {

    @Autowired
    private ISysMenuService sysMenuService;
    @Autowired
    private RemoteFileService remoteFileService;

    /**
     * 获取当前用户信息
     */

    /**
     * 获取当前用户路由表
     */
    @GetMapping("/routers")
    public Object getRouters() {
        List<SysMenu> menuList = sysMenuService.findMenuTreeByUserId(SecurityUtils.getUserId());
        List<RouterVo> routerVoList = sysMenuService.buildMenus(menuList);
        return JsonResult.ok().data(routerVoList);
    }

    /**
     * 更新当前用户信息
     */
    @InsertLog("用户更新信息")
    @PostMapping
    public Object updateUserProfile(@RequestBody SysUser sysUser) {
        new SysUser().setId(SecurityUtils.getUserId())
                .setNickName(sysUser.getNickName())
                .setPhone(sysUser.getPhone())
                .setEmail(sysUser.getEmail())
                .updateById();
        return JsonResult.ok();
    }

    /**
     * 修改当前用户密码
     */
    @InsertLog("用户修改密码")
    @PostMapping("/password")
    public Object updatePwd(@RequestBody SysUserParam sysUserParam) {
        checkReqParamThrowException(sysUserParam.getOldPassword());
        checkReqParamThrowException(sysUserParam.getNewPassword());
        // 验证旧密码
        SysUser user = new SysUser().setId(SecurityUtils.getUserId()).selectById();
        if (!SecurityUtils.matchesPassword(MD5.create().digestHex(sysUserParam.getOldPassword()), user.getPassword())) {
            return JsonResult.error("密码错误");
        }
        boolean b = new SysUser()
                .setId(user.getId())
                .setPassword(SecurityUtils.encryptPassword(MD5.create().digestHex(sysUserParam.getNewPassword())))
                .updateById();
        if (b) return JsonResult.ok();
        else return JsonResult.error();
    }

    /**
     * 上传头像
     */
    @InsertLog("用户更新头像")
    @PostMapping("/avatar")
    public JsonResult uploadAvatar(@RequestParam("file") MultipartFile file) {
        Assert.isTrue(!file.isEmpty());
        LocalFileVo localFileVo = remoteFileService.upload(file, null);
        if (localFileVo == null) {
            return JsonResult.error();
        }
        new SysUser().setId(SecurityUtils.getUserId())
                .setAvatar(localFileVo.getUrl())
                .updateById();
        return JsonResult.ok().data(localFileVo);
    }

}
