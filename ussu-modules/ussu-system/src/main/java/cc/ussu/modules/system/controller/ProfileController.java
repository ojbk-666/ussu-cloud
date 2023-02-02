package cc.ussu.modules.system.controller;

import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.modules.system.entity.SysMenu;
import cc.ussu.modules.system.entity.SysUser;
import cc.ussu.modules.system.entity.vo.RouterVO;
import cc.ussu.modules.system.properties.UssuProperties;
import cc.ussu.modules.system.service.ISysMenuService;
import cc.ussu.modules.system.service.ISysUserService;
import cc.ussu.system.api.model.LoginUser;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SystemLog(serviceName = ServiceNameConstants.SERVICE_SYSETM, group = "个人中心")
@RestController
@RequestMapping("${ussu.mapping-prefix.system}/user/profile")
public class ProfileController extends BaseController {

    @Autowired
    private UssuProperties ussuProperties;
    @Autowired
    private ISysMenuService iSysMenuService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 获取菜单
     */
    @Deprecated
    // @GetMapping("/getRouters")
    public JsonResult getRouters() {
        List<SysMenu> menuList = iSysMenuService.listByUserId(SecurityUtil.getLoginUser().getUserId(),true).stream().filter(r -> !SysMenu.TYPE_BUTTON.equals(r.getType())).collect(Collectors.toList());
        List<RouterVO> routerVos = iSysMenuService.buildRouter(menuList);
        return JsonResult.ok(routerVos);
    }

    /**
     * 获取登录用户信息
     */
    @GetMapping("/getInfo")
    public JsonResult userInfo() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        Map<String, Object> map = BeanUtil.beanToMap(loginUser);
        // 不兼容antd
        map.put("menuList", iSysMenuService.listByUserId(loginUser.getUserId(), false));
        return JsonResult.ok(map);
    }

    /**
     * 更新登录用户信息
     */
    @SystemLog(name = "修改个人信息")
    @PostMapping("/updateInfo")
    public JsonResult updateUserInfo(@RequestBody SysUser p) {
        p.setId(SecurityUtil.getLoginUser().getUserId());
        // 处理头像
        String avatar = p.getAvatar();
        if (StrUtil.isNotBlank(avatar)) {
            if (avatar.startsWith("data:image/")) {
                // base64
                String fileType = avatar.split(";base64")[0].split("image/")[1];
                BufferedImage image = ImgUtil.toImage(avatar.split(";base64,")[1]);
                // a/b.jpg
                String relativePath = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN) + StrPool.SLASH + SecurityUtil.getLoginUser().getUserId() + StrPool.DOT + fileType;
                // D://a/b/c/
                File avatarFile = new File(ussuProperties.getImageDomainUploadPath() + relativePath);
                FileUtil.mkParentDirs(avatarFile);
                ImgUtil.write(image, avatarFile);
                p.setAvatar(ussuProperties.getImageDomain() + relativePath);
                // 处理缓存中的信息
                LoginUser loginUser = SecurityUtil.getLoginUser();
                loginUser.getUser().setAvatar(p.getAvatar())
                        .setNickName(p.getNickName())
                        .setSex(p.getSex() + "")
                        .setEmail(p.getEmail())
                        .setPhone(p.getPhone())
                        // .setRemark(p.getRemark())
                        // .setExt1(p.getExt1()).setExt2(p.getExt2())
                        // .setExt3(p.getExt3()).setExt4(p.getExt4())
                        // .setExt5(p.getExt5()).setExt6(p.getExt6())
                ;
                // securityTokenService.refreshCachedLoginUser(loginUser);
            }
        }
        p.setDisableFlag(null).setAccount(null).setPassword(null).setCreateBy(null).setCreateTime(null)
                .setUpdateBy(null).setUpdateTime(new Date()).setLoginIp(null).setLastLoginTime(null).setUserSort(null).setUserType(null)
                .setVersion(null).setState(null).setDelFlag(null);
        sysUserService.updateById(p);
        return JsonResult.ok();
    }

    /**
     * 更新登录用户头像
     */
    @SystemLog(name = "修改头像")
    @PostMapping("/updateAvatar")
    public JsonResult updateUserAvatar(@RequestPart("avatarfile") MultipartFile multipartFile) throws IOException {
        String userId = SecurityUtil.getLoginUser().getUserId();
        String contentType = multipartFile.getContentType();
        String fileExt = ".png";
        if (contentType.startsWith("image/")) {
            fileExt = StrPool.DOT + StrUtil.removePrefix(contentType, "image/").toLowerCase();
        }
        // 20221111/12.png
        String relativePath = DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN) + StrPool.SLASH + userId + StrPool.DOT + fileExt;
        String filePath = ussuProperties.getImageDomainUploadPath() + relativePath;
        FileUtil.mkParentDirs(filePath);
        multipartFile.transferTo(new File(filePath));
        SysUser p = new SysUser().setId(userId).setAvatar(ussuProperties.getImageDomain() + relativePath);
        sysUserService.updateById(p);
        return JsonResult.ok();
    }

    /**
     * 修改登录用户密码
     */
    @SystemLog(name = "修改密码")
    @PostMapping("/resetPwd")
    public JsonResult updatePwd(@RequestBody Map p) {
        String oldPassword = MapUtil.getStr(p, "oldPassword");
        String newPassword = MapUtil.getStr(p, "newPassword");
        Assert.notBlank(oldPassword, "旧密码不能为空");
        Assert.notBlank(newPassword, "新密码不能为空");
        SysUser user = sysUserService.getById(SecurityUtil.getLoginUser().getUserId());
        Assert.isTrue(SecurityUtil.matchesPassword(oldPassword, user.getPassword()), "旧密码错误");
        sysUserService.updateById(new SysUser().setId(user.getId()).setPassword(SecurityUtil.encryptPassword(newPassword)));
        return JsonResult.ok();
    }

    /**
     * 更新扩展字段
     */
    @PostMapping("/ext")
    public JsonResult updateExt(@RequestBody Map<String, Object> map) {
        String userId = SecurityUtil.getUserId();
        SysUser u = new SysUser().setId(userId);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String k = entry.getKey();
            if (StrUtil.startWith(k, "ext")) {
                try {
                    ReflectUtil.setFieldValue(u, k, entry.getValue());
                } catch (Exception e) {
                }
            }
        }
        sysUserService.updateById(u);
        return JsonResult.ok();
    }

}
