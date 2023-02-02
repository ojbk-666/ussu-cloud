package cc.ussu.auth.controller;

import cc.ussu.auth.model.third.DingTalkLoginParamVO;
import cc.ussu.auth.model.vo.GiteeUserInfoVO;
import cc.ussu.auth.service.impl.ThirdLoginServiceDingTalkImpl;
import cc.ussu.auth.service.impl.ThirdLoginServiceGiteeImpl;
import cc.ussu.auth.vo.LoginParamVO;
import cc.ussu.auth.service.SysLoginService;
import cc.ussu.common.core.constants.CacheConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.log.annotation.SystemLog;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.common.security.util.SecurityUtil;
import cc.ussu.system.api.RemoteSystemUserService;
import cc.ussu.system.api.model.LoginUser;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
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
    @Autowired(required = false)
    private ThirdLoginServiceGiteeImpl thirdLoginServiceGitee;
    @Autowired(required = false)
    private ThirdLoginServiceDingTalkImpl thirdLoginServiceDingTalk;
    @Autowired
    private RemoteSystemUserService remoteSystemUserService;

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
        LoginUser loginUser = SecurityUtil.getLoginUser();
        String userId = loginUser.getUserId();
        // 查询用户信息
        JsonResult<Map<String, Object>> jrUser = remoteSystemUserService.getUserById(userId);
        Map<String, Object> userMap = jrUser.getData();
        Map<String, Object> info = BeanUtil.beanToMap(loginUser);
        info.put("user", userMap);
        return JsonResult.ok(info);
    }

    /**
     * 绑定三方账号
     */
    @PostMapping("/bind")
    public JsonResult bindThirdAccount(@RequestBody Map<String, Object> map) throws Exception {
        String bindtype = MapUtil.getStr(map, "bindtype");
        JsonResult<LoginUser> jr = null;
        Map<String, Object> updateMap = new HashMap<>();
        if ("dingtalk".equals(bindtype)) {
            DingTalkLoginParamVO paramVO = BeanUtil.toBean(map, DingTalkLoginParamVO.class);
            GetUserResponseBody userinfo = thirdLoginServiceDingTalk.exchangeThirdAccountInfo(paramVO);
            String unionId = userinfo.getUnionId();
            updateMap.put("ext1", unionId);
            // 检查已有账号
            jr = remoteSystemUserService.getUserByExt1(unionId, false);
        } else if ("gitee".equals(bindtype)) {
            GiteeUserInfoVO giteeUserInfoVO = thirdLoginServiceGitee.exchangeThirdAccountInfo(MapUtil.getStr(map, "code"), false);
            updateMap.put("ext2", giteeUserInfoVO.getLogin());
            jr = remoteSystemUserService.getUserByExt2(giteeUserInfoVO.getLogin(), false);
        } else if ("alipay".equals(bindtype)) {
            updateMap.put("ext3", "");
            jr = remoteSystemUserService.getUserByExt3("阿里用户id", false);
        } else if ("qq".equals(bindtype)) {
        } else if ("wechat".equals(bindtype)) {
        }
        if (jr.isOk()) {
            String id = jr.getData().getUser().getId();
            if (!SecurityUtil.getUserId().equals(id)) {
                // 已绑定其他账号
                return JsonResult.error(null,"已绑定其他账号");
            } else {
                // 已绑定当前账号
                return JsonResult.ok(null, "已绑定");
            }
        } else {
            // 可以绑定
            remoteSystemUserService.updateUserExt(updateMap);
            return JsonResult.ok(null, "绑定成功");
        }
        // return JsonResult.ok(null, "绑定成功");
    }

}
