package cn.ussu.auth.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.ussu.auth.feign.RemoteSystemUserService;
import cn.ussu.auth.model.param.login.third.AlipayThirdLoginParam;
import cn.ussu.auth.properties.ThirdLoginAlipayProperties;
import cn.ussu.auth.service.SysLoginService;
import cn.ussu.common.core.base.BaseController;
import cn.ussu.common.core.constants.CacheConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.security.entity.LoginUser;
import cn.ussu.common.security.entity.SysUser;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 三方账号登录
 */
@RestController
@RequestMapping("/login/third")
public class ThirdLoginController extends BaseController {

    @Autowired
    private ThirdLoginAlipayProperties thirdLoginAlipayProperties;
    @Autowired
    private RemoteSystemUserService remoteSystemUserService;
    @Autowired
    private SysLoginService sysLoginService;

    /**
     * 支付宝登录
     */
    @PostMapping("/alipay")
    public Object alipayLogin(@RequestBody AlipayThirdLoginParam alipayThirdLoginParam) throws Exception {
        /*String auth_code = alipayThirdLoginParam.getAuth_code();
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
        }*/
        AlipayUserInfoShareResponse alipayUserInfoShareResponse = new AlipayUserInfoShareResponse();
        alipayUserInfoShareResponse.setUserId("2088612739711827");
        alipayUserInfoShareResponse.setNickName("zzz");
        alipayUserInfoShareResponse.setAvatar("https://tfs.alipayobjects.com/images/partner/TB1XLr.bKxFDuNjm2EuXXaJ6pXa");
        alipayUserInfoShareResponse.setGender("m");
        alipayUserInfoShareResponse.setProvince("山东");
        alipayUserInfoShareResponse.setCity("济南");
        // 写入用户信息
        Map<String, Object> param = getNewHashMap();
        param.put("userId", alipayUserInfoShareResponse.getUserId());
        param.put("nickName", alipayUserInfoShareResponse.getNickName());
        param.put("avatar", alipayUserInfoShareResponse.getAvatar());
        param.put("gender", alipayUserInfoShareResponse.getGender());
        param.put("province", alipayUserInfoShareResponse.getProvince());
        param.put("city", alipayUserInfoShareResponse.getCity());
        JsonResult jsonResult = remoteSystemUserService.insertOrUpdateByThirdAlipay(param);
        SysUser newUser = BeanUtil.toBean(jsonResult.getData(), SysUser.class);
        // 返回生成的token
        Map<String, Object> data = getNewHashMap();
        String tokenStr = null;
        if (newUser != null) {
            LoginUser loginUser = sysLoginService.convertToLoginUser(newUser);
            tokenStr = loginUser.getToken();
        }
        data.put(CacheConstants.TOKEN_IN_REQUEST_KEY, tokenStr);
        return JsonResult.ok().data(data);
    }

}
