package cc.ussu.auth.service.impl;

import cc.ussu.auth.model.vo.GiteeAccessTokenResultVO;
import cc.ussu.auth.model.vo.GiteeUserInfoVO;
import cc.ussu.auth.properties.ThirdLoginGiteeProperties;
import cc.ussu.auth.service.SysLoginService;
import cc.ussu.auth.service.ThirdLoginService;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.system.api.RemoteSystemUserService;
import cc.ussu.system.api.model.LoginUser;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ThirdLoginServiceGiteeImpl implements ThirdLoginService<String> {

    @Autowired
    private ThirdLoginGiteeProperties thirdLoginGiteeProperties;
    @Autowired
    private RemoteSystemUserService remoteSystemUserService;
    @Autowired
    private SysLoginService sysLoginService;

    @Override
    public JsonResult login(String code) throws Exception {
        GiteeUserInfoVO giteeUserInfoVO = exchangeThirdAccountInfo(code);
        // todo 注册或拉取用户信息
        JsonResult<LoginUser> jr = remoteSystemUserService.getUserByExt2(giteeUserInfoVO.getLogin(), true);
        if (jr.isOk()) {
            LoginUser loginUser = jr.getData();
            // 返回生成的token
            String tokenStr = sysLoginService.convertToLoginUser(loginUser);
            return JsonResult.ok(tokenStr, null);
        } else {
            return jr;
        }
    }

    @Override
    public GiteeUserInfoVO exchangeThirdAccountInfo(String code) throws Exception {
        return exchangeThirdAccountInfo(code, true);
    }

    public GiteeUserInfoVO exchangeThirdAccountInfo(String code, boolean isLogin) throws Exception {
        GiteeAccessTokenResultVO accessToken = getAccessToken(code, isLogin ? thirdLoginGiteeProperties.getRedirectUriLogin() : thirdLoginGiteeProperties.getRedirectUriBind());
        if (accessToken.isSuccess()) {
            // 换取用户信息
            GiteeUserInfoVO userInfo = getUserInfo(accessToken.getAccess_token());
            return userInfo;
        } else {
            throw new IllegalArgumentException("登录失败请重试:" + accessToken.getError_description());
        }
    }

    /**
     * 获取token 108f446fccf4bc77b649e5d6375e052f
     */
    private GiteeAccessTokenResultVO getAccessToken(String code, String redirectUrl) {
        Map<String, Object> param = BeanUtil.beanToMap(thirdLoginGiteeProperties, true, false);
        String post = HttpUtil.post("https://gitee.com/oauth/token?client_id=" + thirdLoginGiteeProperties.getClientId() + "&code=" + code + "&grant_type=authorization_code&redirect_uri=" + URLUtil.encodeQuery(redirectUrl), param);
        return JSON.parseObject(post, GiteeAccessTokenResultVO.class);
    }

    /**
     * 获取用户信息
     */
    private GiteeUserInfoVO getUserInfo(String accessToken) {
        String s = HttpUtil.get("https://gitee.com/api/v5/user?access_token=" + accessToken);
        return JSON.parseObject(s, GiteeUserInfoVO.class);
    }

}
