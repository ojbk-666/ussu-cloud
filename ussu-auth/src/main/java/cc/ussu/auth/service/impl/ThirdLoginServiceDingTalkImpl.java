package cc.ussu.auth.service.impl;

import cc.ussu.auth.model.third.DingTalkLoginParamVO;
import cc.ussu.auth.properties.ThirdLoginDingTalkProperties;
import cc.ussu.auth.service.SysLoginService;
import cc.ussu.auth.service.ThirdLoginService;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.system.api.RemoteSystemUserService;
import cc.ussu.system.api.model.LoginUser;
import com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponse;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThirdLoginServiceDingTalkImpl implements ThirdLoginService<DingTalkLoginParamVO> {

    @Autowired
    private ThirdLoginDingTalkProperties dingTalkProperties;
    @Autowired
    private RemoteSystemUserService remoteSystemUserService;
    @Autowired
    private SysLoginService sysLoginService;

    @Override
    public JsonResult login(DingTalkLoginParamVO paramVO) throws Exception {
        GetUserResponseBody userinfo = exchangeThirdAccountInfo(paramVO);
        JsonResult<LoginUser> jr = remoteSystemUserService.getUserByExt1(userinfo.getUnionId(), true);
        if (jr.isOk()) {
            String tokenStr = sysLoginService.convertToLoginUser(jr.getData());
            return JsonResult.ok(tokenStr);
        } else {
            return JsonResult.error("没有绑定的账号");
        }
    }

    @Override
    public GetUserResponseBody exchangeThirdAccountInfo(DingTalkLoginParamVO paramVO) throws Exception {
        String authCode = paramVO.getAuthCode();
        com.aliyun.dingtalkoauth2_1_0.Client client = authClient();
        GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()
            //应用基础信息-应用信息的AppKey,请务必替换为开发的应用AppKey
            .setClientId(dingTalkProperties.getAppKey())
            //应用基础信息-应用信息的AppSecret，,请务必替换为开发的应用AppSecret
            .setClientSecret(dingTalkProperties.getAppSecret())
            .setCode(authCode)
            .setGrantType("authorization_code");
        GetUserTokenResponse getUserTokenResponse = client.getUserToken(getUserTokenRequest);
        // 获取用户个人token
        String accessToken = getUserTokenResponse.getBody().getAccessToken();
        GetUserResponseBody userinfo = getUserinfo(accessToken);
        return userinfo;
    }

    public static com.aliyun.dingtalkoauth2_1_0.Client authClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }

    public static com.aliyun.dingtalkcontact_1_0.Client contactClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkcontact_1_0.Client(config);
    }

    /**
     * 获取用户个人信息
     *
     * @param accessToken
     * @return
     * @throws Exception
     */
    public GetUserResponseBody getUserinfo(String accessToken) throws Exception {
        com.aliyun.dingtalkcontact_1_0.Client client = contactClient();
        GetUserHeaders getUserHeaders = new GetUserHeaders();
        getUserHeaders.xAcsDingtalkAccessToken = accessToken;
        //获取用户个人信息
        GetUserResponse getUserResponse = client.getUserWithOptions("me", getUserHeaders, new RuntimeOptions());
        GetUserResponseBody body = getUserResponse.getBody();
        return body;
    }
}
