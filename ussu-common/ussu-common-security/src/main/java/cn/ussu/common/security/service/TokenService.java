package cn.ussu.common.security.service;

import cn.hutool.core.lang.UUID;
import cn.ussu.common.core.constants.CacheConstants;
import cn.ussu.common.core.util.HttpContext;
import cn.ussu.common.security.util.SecurityUtils;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.common.security.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class TokenService {

    protected static final long MILLIS_SECOND = 1000;
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    @Autowired
    private RedisService redisService;

    /**
     * 创建token
     */
    public String createToken(LoginUser loginUser) {
        String token = UUID.fastUUID().toString();
        loginUser.setToken(token);
        long now = new Date().getTime();
        loginUser.setLoginTime(now);
        loginUser.setExpireTime(now + (MILLIS_MINUTE * 5));
        refreshToken(loginUser);
        return token;
    }

    /**
     * 删除token
     */

    /**
     * 刷新token有效时间,有效期小于一半才刷新
     */
    public void refreshToken(LoginUser loginUser) {
        Long expireTime = loginUser.getExpireTime();
        // 如果小于一半时间则刷新
        long now = new Date().getTime();
        if ((expireTime - now) <= (30 * MILLIS_MINUTE)) {
            loginUser.setExpireTime(60 * MILLIS_MINUTE + now);
            redisService.setCacheObject(CacheConstants.LOGIN_TOKEN_KEY_ + loginUser.getToken(), loginUser, MILLIS_MINUTE * 60);
        }
    }

    /**
     * 创建登录用户
     */

    /**
     * 获取登录用户
     */
    public LoginUser getLoginUser() {
        return getLoginUser(HttpContext.getRequest());

    }
    /**
     * 获取登录用户
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        String requestToken = SecurityUtils.getRequestToken(request);
        LoginUser loginUser = redisService.getCacheObject(CacheConstants.LOGIN_TOKEN_KEY_ + requestToken);
        return loginUser;
    }

    /**
     * 删除登录用户
     */

}
