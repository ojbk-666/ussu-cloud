package cn.ussu.auth.controller;

import cn.ussu.auth.entity.param.LoginParam;
import cn.ussu.auth.service.SysLoginService;
import cn.ussu.common.core.constants.CacheConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.common.security.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录 登出
 */
@RestController
@RequestMapping
public class TokenController {

    @Autowired
    private SysLoginService sysLoginService;
    @Autowired
    private RedisService redisService;

    @PostMapping("/login")
    public JsonResult login(@RequestBody LoginParam loginParam) {
        LoginUser loginUser = sysLoginService.login(loginParam);
        return JsonResult.ok().put(CacheConstants.TOKEN_IN_REQUEST_KEY, loginUser.getToken());
    }

    @DeleteMapping("/logout")
    public JsonResult logout() {
        return JsonResult.ok();
    }

}
