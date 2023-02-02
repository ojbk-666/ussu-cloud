package cc.ussu.auth.controller;

import cc.ussu.auth.model.third.DingTalkLoginParamVO;
import cc.ussu.auth.model.third.GiteeLoginParamVO;
import cc.ussu.auth.properties.ThirdLoginDingTalkProperties;
import cc.ussu.auth.properties.ThirdLoginGiteeProperties;
import cc.ussu.auth.service.impl.ThirdLoginServiceDingTalkImpl;
import cc.ussu.auth.service.impl.ThirdLoginServiceGiteeImpl;
import cc.ussu.common.core.constants.ServiceNameConstants;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.log.annotation.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 三方账号登录
 */
@SystemLog(serviceName = ServiceNameConstants.SERVICE_AUTH, group = "第三方账号登录")
@RestController
@RequestMapping("/login/third")
public class ThirdLoginController extends BaseController {

    @Autowired(required = false)
    private ThirdLoginServiceGiteeImpl thirdLoginServiceGitee;
    @Autowired(required = false)
    private ThirdLoginServiceDingTalkImpl thirdLoginServiceDingTalk;
    @Autowired
    private ThirdLoginGiteeProperties giteeProperties;
    @Autowired
    private ThirdLoginDingTalkProperties dingTalkProperties;

    /**
     * Gitee
     */
    @SystemLog(name = "Gitee登录")
    @PostMapping("/gitee")
    public JsonResult giteeCode2Token(@Validated @RequestBody GiteeLoginParamVO paramVO) throws Exception {
        return thirdLoginServiceGitee.login(paramVO.getCode());
    }

    /**
     * Gitee
     */
    @SystemLog(name = "Gitee登录")
    @GetMapping("/gitee")
    public void gitee(GiteeLoginParamVO param, HttpServletResponse response) throws Exception {
        JsonResult<String> jr = thirdLoginServiceGitee.login(param.getCode());
        String redirectUri = giteeProperties.getRedirectUriLogin();
        if (jr.isOk()) {
            redirectUri += "?set-token=" + jr.getData();
        }
        response.sendRedirect(redirectUri);
    }

    /**
     * 钉钉登录
     */
    @PostMapping("/dingtalk")
    public JsonResult dingtalkCode2Token(@Validated @RequestBody DingTalkLoginParamVO param) throws Exception {
        return thirdLoginServiceDingTalk.login(param);
    }

    /**
     * 钉钉登录
     */
    @GetMapping("/dingtalk")
    public void dingtalk(DingTalkLoginParamVO param, HttpServletResponse response) throws Exception {
        JsonResult jr = thirdLoginServiceDingTalk.login(param);
        String redirectUri = dingTalkProperties.getRedirectUri();
        if (jr.isOk()) {
            redirectUri += "?set-token=" + jr.getData();
        }
        response.sendRedirect(redirectUri);
    }

}
