package cn.ussu.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import cn.ussu.auth.feign.RemoteSystemUserService;
import cn.ussu.auth.model.param.login.ThirdLoginToSysUserParam;
import cn.ussu.auth.model.param.login.third.GiteeThirdLoginParam;
import cn.ussu.auth.model.vo.GiteeAccessTokenResultVo;
import cn.ussu.auth.model.vo.GiteeUserInfoVo;
import cn.ussu.auth.properties.ThirdLoginGiteeProperties;
import cn.ussu.auth.service.SysLoginService;
import cn.ussu.auth.service.ThirdLoginService;
import cn.ussu.common.core.constants.CacheConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.security.entity.LoginUser;
import cn.ussu.common.security.entity.SysUser;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ThirdLoginServiceGiteeImpl implements ThirdLoginService<GiteeThirdLoginParam> {

    @Autowired
    private ThirdLoginGiteeProperties thirdLoginGiteeProperties;
    @Autowired
    private RemoteSystemUserService remoteSystemUserService;
    @Autowired
    private SysLoginService sysLoginService;

    @Override
    public JsonResult login(GiteeThirdLoginParam param) throws Exception {
        Assert.notBlank(param.getCode());
        GiteeAccessTokenResultVo accessToken = getAccessToken(param.getCode());
        if (accessToken.isSuccess()) {
            // 换取用户信息
            GiteeUserInfoVo userInfo = getUserInfo(accessToken.getAccess_token());
            // 注册或拉取用户信息
            ThirdLoginToSysUserParam p = new ThirdLoginToSysUserParam()
                    .setAccount("gitee_" + userInfo.getLogin())
                    .setName(userInfo.getName())
                    .setNickName(userInfo.getName())
                    .setAvatar(userInfo.getAvatar_url())
                    .setEmail(userInfo.getEmail())
                    .setSex(3)
                    .setSource(6);
            Object o = remoteSystemUserService.insertOrUpdateByThird(p);
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
        } else {
            throw new IllegalArgumentException("登录失败请重试:" + accessToken.getError_description());
        }
    }

    /**
     * 获取token 108f446fccf4bc77b649e5d6375e052f
     */
    private GiteeAccessTokenResultVo getAccessToken(String code) {
        Assert.notBlank(code);
        Map<String, Object> param = BeanUtil.beanToMap(thirdLoginGiteeProperties, true, false);
        // param.put("redirect_uri", URLUtil.encodeQuery(thirdLoginGiteeProperties.getRedirectUri()));
        String post = HttpUtil.post("https://gitee.com/oauth/token?client_id=" + thirdLoginGiteeProperties.getClientId() + "&code=" + code + "&grant_type=authorization_code&redirect_uri=" + URLUtil.encodeQuery(thirdLoginGiteeProperties.getRedirectUri()), param);
        return JSON.parseObject(post, GiteeAccessTokenResultVo.class);
    }

    /**
     * 获取用户信息
     */
    private GiteeUserInfoVo getUserInfo(String accessToken) {
        String s = HttpUtil.get("https://gitee.com/api/v5/user?access_token=" + accessToken);
        return JSON.parseObject(s, GiteeUserInfoVo.class);
    }

}
