package cc.ussu.auth.controller;

import cc.ussu.auth.vo.LoginParamVO;
import cc.ussu.auth.service.SysLoginService;
import cc.ussu.common.core.constants.CacheConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.common.security.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    private static final String SYSTEM_LOG_GROUP = "个人中心";

    @SystemLog(group = SYSTEM_LOG_GROUP, name = "登录")
    @PostMapping("/login")
    public JsonResult login(@Validated @RequestBody LoginParamVO loginParamVO) {
        String tokenStr = sysLoginService.login(loginParamVO);
        Map<String, Object> data = new HashMap<>();
        data.put("access_token", tokenStr);
        return JsonResult.ok(data);
    }

    @SystemLog(group = SYSTEM_LOG_GROUP, name = "退出登录")
    @RequestMapping("/logout")
    public JsonResult logout() {
        redisService.deleteObject(CacheConstants.LOGIN_TOKEN_KEY_ + SecurityUtil.getRequestToken());
        return JsonResult.ok();
    }

    @GetMapping("/info")
    public JsonResult getUserInfo() {
        return JsonResult.ok(SecurityUtil.getLoginUser());
    }

}
