package cn.ussu.auth.controller;

import cn.ussu.auth.model.param.login.third.AlipayThirdLoginParam;
import cn.ussu.auth.model.param.login.third.GiteeThirdLoginParam;
import cn.ussu.auth.service.impl.ThirdLoginServiceAlipayImpl;
import cn.ussu.auth.service.impl.ThirdLoginServiceGiteeImpl;
import cn.ussu.common.core.base.BaseController;
import cn.ussu.common.log.annotation.InsertLog;
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

    @Autowired
    private ThirdLoginServiceAlipayImpl thirdLoginServiceAlipay;
    @Autowired
    private ThirdLoginServiceGiteeImpl thirdLoginServiceGitee;

    /**
     * 支付宝登录
     */
    @InsertLog("支付宝登录")
    @PostMapping("/alipay")
    public Object alipayLogin(@RequestBody AlipayThirdLoginParam alipayThirdLoginParam) throws Exception {
        return thirdLoginServiceAlipay.login(alipayThirdLoginParam);
    }

    /**
     * gitee
     */
    @InsertLog("Gitee登录")
    @PostMapping("/gitee")
    public Object gitee(@RequestBody GiteeThirdLoginParam param) throws Exception{
        return thirdLoginServiceGitee.login(param);
    }

}
