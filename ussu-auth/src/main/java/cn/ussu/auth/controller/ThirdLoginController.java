package cn.ussu.auth.controller;

import cn.ussu.auth.model.param.login.third.AlipayThirdLoginParam;
import cn.ussu.auth.properties.ThirdLoginAlipayProperties;
import cn.ussu.common.core.base.BaseController;
import cn.ussu.common.core.entity.JsonResult;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
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
    private ThirdLoginAlipayProperties thirdLoginAlipayProperties;

    /**
     * 支付宝登录
     */
    @PostMapping("/alipay")
    public Object alipayLogin(@RequestBody AlipayThirdLoginParam alipayThirdLoginParam) throws Exception {
        String auth_code = alipayThirdLoginParam.getAuth_code();
        String app_id = alipayThirdLoginParam.getApp_id();
        checkReqParamThrowException(auth_code);
        if (!thirdLoginAlipayProperties.getAppid().equals(app_id)) {
            throw new IllegalArgumentException("appid不一致");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(thirdLoginAlipayProperties.getAlipayGateway()
                , thirdLoginAlipayProperties.getAppid()
                , thirdLoginAlipayProperties.getPrivateKey()
                , "json", "UTF-8"
                , thirdLoginAlipayProperties.getAlipayPublicKey()
                , "RSA2");
        // 换取accessToken
        AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
        oauthTokenRequest.setCode(auth_code);
        oauthTokenRequest.setGrantType("authorization_code");
        AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);
        if (!oauthTokenResponse.isSuccess()) {
            System.out.println(oauthTokenResponse.getSubMsg());
            throw new RuntimeException(oauthTokenResponse.getSubMsg());
        }
        String accessToken = oauthTokenResponse.getAccessToken();
        System.out.println("accessToken:" + accessToken);
        // 获取用户信息
        AlipayUserInfoShareRequest alipayUserInfoShareRequest = new AlipayUserInfoShareRequest();
        AlipayUserInfoShareResponse alipayUserInfoShareResponse = alipayClient.execute(alipayUserInfoShareRequest, accessToken);
        if (!alipayUserInfoShareResponse.isSuccess()) {
            System.out.println(alipayUserInfoShareResponse.getSubMsg());
            throw new RuntimeException(alipayUserInfoShareResponse.getSubMsg());
        }
        // 写入用户信息

        System.out.println(String.format("用户头像地址：%s，id：%s，uname：%s", alipayUserInfoShareResponse.getUserId(), alipayUserInfoShareResponse.getNickName(), alipayUserInfoShareResponse.getAvatar()));
        return JsonResult.ok().data(alipayUserInfoShareResponse);
    }

}
