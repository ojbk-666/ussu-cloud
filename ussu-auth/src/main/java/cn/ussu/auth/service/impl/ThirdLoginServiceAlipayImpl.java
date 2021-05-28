package cn.ussu.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.ussu.auth.feign.RemoteSystemUserService;
import cn.ussu.auth.model.param.login.ThirdLoginToSysUserParam;
import cn.ussu.auth.model.param.login.third.AlipayThirdLoginParam;
import cn.ussu.auth.properties.ThirdLoginAlipayProperties;
import cn.ussu.auth.service.SysLoginService;
import cn.ussu.auth.service.ThirdLoginService;
import cn.ussu.common.core.constants.CacheConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.security.entity.LoginUser;
import cn.ussu.common.security.entity.SysUser;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 支付宝登录
 *
 * @author liming
 * @date 2021-05-27 23:11
 */
@Service
public class ThirdLoginServiceAlipayImpl implements ThirdLoginService<AlipayThirdLoginParam> {

    @Autowired
    private ThirdLoginAlipayProperties thirdLoginAlipayProperties;
    @Autowired
    private RemoteSystemUserService remoteSystemUserService;
    @Autowired
    private SysLoginService sysLoginService;

    @Override
    public JsonResult login(AlipayThirdLoginParam alipayThirdLoginParam) throws Exception {
        String auth_code = alipayThirdLoginParam.getAuth_code();
        String app_id = alipayThirdLoginParam.getApp_id();
        Assert.notBlank(auth_code);
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
        Map<String, Object> param = getNewHashMap();
        param.put("userId", alipayUserInfoShareResponse.getUserId());
        param.put("nickName", alipayUserInfoShareResponse.getNickName());
        param.put("avatar", alipayUserInfoShareResponse.getAvatar());
        param.put("gender", alipayUserInfoShareResponse.getGender());
        param.put("province", alipayUserInfoShareResponse.getProvince());
        param.put("city", alipayUserInfoShareResponse.getCity());
        ThirdLoginToSysUserParam registerParam = new ThirdLoginToSysUserParam();
        registerParam.setAccount("alipay_" + alipayUserInfoShareResponse.getUserId())
                .setAvatar(alipayUserInfoShareResponse.getAvatar())
                .setName(alipayUserInfoShareResponse.getNickName())
                .setNickName(alipayUserInfoShareResponse.getNickName())
                .setSex("m".equals(alipayUserInfoShareResponse.getGender()) ? 1 : 2)
                .setEmail("")
                .setSource(5);
        Object o = remoteSystemUserService.insertOrUpdateByThird(registerParam);
        SysUser newUser = BeanUtil.toBean(o, SysUser.class);
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
