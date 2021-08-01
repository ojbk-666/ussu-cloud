package cn.ussu.auth.controller;

import cn.ussu.auth.model.param.login.system.LoginParam;
import cn.ussu.auth.service.SysLoginService;
import cn.ussu.common.core.base.BaseController;
import cn.ussu.common.core.constants.CacheConstants;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.log.annotation.InsertLog;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.common.security.entity.LoginUser;
import cn.ussu.common.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 登录 登出
 */
@RestController
@RequestMapping
public class TokenController extends BaseController {

    @Autowired
    private SysLoginService sysLoginService;
    @Autowired
    private RedisService redisService;

    @InsertLog("用户登录")
    @PostMapping("/login")
    public JsonResult login(@RequestBody LoginParam loginParam) {
        LoginUser loginUser = sysLoginService.login(loginParam);
        Map<String, Object> data = getNewHashMap();
        data.put(CacheConstants.TOKEN_IN_REQUEST_KEY, loginUser.getToken());
        data.put("nickName", loginUser.getSysUser().getNickName());
        data.put("avatar", loginUser.getSysUser().getAvatar());
        return JsonResult.ok().data(data);
    }

    @InsertLog("用户退出登录")
    @RequestMapping("/logout")
    public JsonResult logout() {
        redisService.deleteObject(CacheConstants.LOGIN_TOKEN_KEY_ + SecurityUtils.getRequestToken());
        return JsonResult.ok();
    }

    @GetMapping("/info")
    public JsonResult getUserInfo() {
        return JsonResult.ok().data(SecurityUtils.getLoginUser());
    }

}
