package cn.ussu.auth.service;

import cn.hutool.core.lang.Assert;
import cn.ussu.auth.entity.param.LoginParam;
import cn.ussu.auth.feign.RemoteSystemUserService;
import cn.ussu.common.security.entity.LoginUser;
import cn.ussu.common.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLoginService {

    @Autowired
    private RemoteSystemUserService remoteSystemUserService;
    @Autowired
    private TokenService tokenService;

    public LoginUser login(LoginParam loginParam) {
        String username = loginParam.getUsername();
        String password = loginParam.getPassword();
        Assert.notBlank(username, "用户名或密码不能为空");
        Assert.notBlank(password, "用户名或密码不能为空");
        // 验证码
        // 验证账户
        // remoteSystemUserService.getUserByUsername(username);
        LoginUser loginUser = new LoginUser();
        String token = tokenService.createToken(loginUser);
        return loginUser;
    }

}
