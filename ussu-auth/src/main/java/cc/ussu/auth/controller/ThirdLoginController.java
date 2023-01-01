package cc.ussu.auth.controller;

import cc.ussu.auth.model.param.login.third.GiteeThirdLoginParam;
import cc.ussu.auth.service.impl.ThirdLoginServiceGiteeImpl;
import cc.ussu.common.core.web.controller.BaseController;
import cc.ussu.common.log.annotation.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 三方账号登录
 */
@RestController
@RequestMapping("/login/third")
public class ThirdLoginController extends BaseController {

    @Autowired(required = false)
    private ThirdLoginServiceGiteeImpl thirdLoginServiceGitee;

    /**
     * gitee
     */
    @SystemLog(group = "第三方账号登录", name = "Gitee登录")
    @PostMapping("/gitee")
    public Object gitee(@RequestBody GiteeThirdLoginParam param) throws Exception{
        return thirdLoginServiceGitee.login(param);
    }

}
