package cc.ussu.common.security.service;

import cc.ussu.common.core.constants.*;
import cc.ussu.common.core.exception.ServiceException;
import cc.ussu.common.core.util.JwtUtils;
import cc.ussu.common.redis.service.RedisService;
import cc.ussu.common.security.util.SecurityUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cc.ussu.common.core.util.HttpContextHolder;
import cc.ussu.common.core.vo.JsonResult;
import cc.ussu.system.api.model.LoginUser;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    protected static final long MILLIS_SECOND = 1000;
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;
    private final static long expireTime = CacheConstants.EXPIRATION;
    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY_;
    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    @Autowired
    private RedisService redisService;

    /**
     * 创建token
     */
    public String createToken(LoginUser loginUser) {
        String uuid = UUID.fastUUID().toString(true);
        loginUser.setToken(uuid);

        refreshToken(loginUser);

        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, uuid);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, loginUser.getUserId());
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, loginUser.getAccount());

        String accessToken = JwtUtils.createToken(claimsMap);
        return accessToken;
    }

    /**
     * 创建token
     */
    public Map<String, Object> createTokenMap(LoginUser loginUser) {
        String accessToken = createToken(loginUser);
        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", accessToken);
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUser
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新token有效时间
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * jwt字符串解析
     *
     * @param tokenStr
     * @return
     * @throws ExpiredJwtException
     * @throws UnsupportedJwtException
     * @throws MalformedJwtException
     * @throws SignatureException
     * @throws IllegalArgumentException
     */
    protected Claims parseToken(String tokenStr) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        if (tokenStr.startsWith(CacheConstants.TOKEN_PREFIX)) {
            tokenStr = tokenStr.substring(CacheConstants.TOKEN_PREFIX.length());
        }
        JwtParser jwtParser = Jwts.parser().setSigningKey(TokenConstants.SECRET);
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(tokenStr);
        Claims claims = claimsJws.getBody();
        return claims;
    }

    /**
     * 获取token荷载
     *
     * @param tokenStr
     * @return
     */
    public Claims checkToken(String tokenStr) {
        if (StrUtil.isBlank(tokenStr)) {
            return null;
        }
        tokenStr = URLUtil.decode(tokenStr);
        if (tokenStr.startsWith(CacheConstants.TOKEN_PREFIX)) {
            tokenStr = tokenStr.substring(CacheConstants.TOKEN_PREFIX.length());
        }
        try {
            return this.parseToken(tokenStr);
        } catch (Exception e) {
            throw new ServiceException(ErrorCodeConstants.TIME_OUT, ErrorMsgConstants.TIME_OUT);
        }
    }

    /**
     * 获取token检查结果
     *
     * @param tokenStr
     * @return
     */
    public JsonResult checkTokenResult(String tokenStr) {
        try {
            Claims body = this.parseToken(tokenStr);
            return JsonResult.ok(body);
        } catch (ExpiredJwtException e) {
            return JsonResult.error(ErrorCodeConstants.TIME_OUT, ErrorMsgConstants.TIME_OUT);
        } catch (Exception e) {
            return JsonResult.error(ErrorCodeConstants.UNAUTHORIZED, ErrorMsgConstants.UNAUTHORIZED);
        }
    }

    /**
     * 创建登录用户
     */

    /**
     * 获取登录用户
     */
    public LoginUser getLoginUser() {
        return getLoginUser(HttpContextHolder.getRequest());
    }

    public LoginUser getLoginUser(String token) {
        LoginUser user = null;
        try {
            if (StrUtil.isNotBlank(token)) {
                String userkey = JwtUtils.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userkey));
                return user;
            }
        } catch (Exception e) {
        }
        return user;
    }

    /**
     * 获取登录用户
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String requestToken = SecurityUtil.getRequestToken(request, true);
        return getLoginUser(requestToken);
    }

    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }

    /**
     * 删除登录用户
     */

}
