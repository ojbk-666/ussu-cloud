package cn.ussu.common.security.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.ussu.common.core.constants.CacheConstants;
import cn.ussu.common.core.constants.ErrorCodeConstants;
import cn.ussu.common.core.constants.ErrorMsgConstants;
import cn.ussu.common.core.exception.ServiceException;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.core.util.HttpContext;
import cn.ussu.common.redis.service.RedisService;
import cn.ussu.common.security.entity.LoginUser;
import cn.ussu.common.security.entity.SysUser;
import cn.ussu.common.security.properties.JwtTokenProperties;
import cn.ussu.common.security.util.SecurityUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    protected static final long MILLIS_SECOND = 1000;
    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    @Autowired
    private RedisService redisService;
    @Autowired
    private JwtTokenProperties jwtTokenProperties;

    public String createToken(Map param, boolean containsExpireTime) {
        DefaultClaims defaultClaims = new DefaultClaims();
        defaultClaims.putAll(param);
        Date now = new Date();
        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(defaultClaims)
                .setIssuedAt(now)
                .setSubject(((String) param.get("userId")))
                .setIssuer("system")
                .signWith(SignatureAlgorithm.HS256, jwtTokenProperties.getKey().getBytes());
        if (containsExpireTime) {
            DateTime expireTime = DateUtil.offsetMinute(now, jwtTokenProperties.getTimeout());
            jwtBuilder.setExpiration(expireTime);
        }
        String token = jwtBuilder.compact();
        return CacheConstants.TOKEN_PREFIX + token;
    }

    /**
     * 创建token
     */
    public String createToken(LoginUser loginUser) {
        // String token = UUID.fastUUID().toString();
        // loginUser.setToken(token);
        boolean userType1 = loginUser.getSysUser().getUserType().equals(1);
        Map<String, Object> payloadParam = new HashMap<>();
        payloadParam.put("userId", loginUser.getSysUser().getId());
        payloadParam.put("userType", loginUser.getSysUser().getUserType());
        payloadParam.put("name", loginUser.getSysUser().getName());
        payloadParam.put("nickName", loginUser.getSysUser().getNickName());
        String token = createToken(payloadParam, !userType1);
        loginUser.setToken(token);
        long now = new Date().getTime();
        loginUser.setLoginTime(now);
        if (userType1) {
            loginUser.setExpireTime(now + (MILLIS_MINUTE * jwtTokenProperties.getTimeout()));
            // 缓存至token
            cacheSystemUserToRedis(loginUser);
        } else {
            loginUser.setExpireTime(checkToken(token).getExpiration().getTime());
        }
        return token;
    }

    protected void cacheSystemUserToRedis(LoginUser loginUser) {
        String key = CacheConstants.LOGIN_TOKEN_KEY_ + StrUtil.removePrefix(loginUser.getToken(), CacheConstants.TOKEN_PREFIX);
        redisService.setCacheObject(key, loginUser, MILLIS_MINUTE * jwtTokenProperties.getTimeout());
    }

    /**
     * 删除token
     */

    /**
     * todo 刷新token有效时间,有效期小于一半才刷新
     */
    public void refreshToken(LoginUser loginUser) {
        Long expireTime = loginUser.getExpireTime();
        // 如果小于一半时间则刷新
        long now = new Date().getTime();
        Integer timeout = jwtTokenProperties.getTimeout();
        if ((expireTime - now) <= ((timeout / 2) * MILLIS_MINUTE)) {
            loginUser.setExpireTime(timeout * MILLIS_MINUTE + now);
            // 系统用户缓存到redis
            if (loginUser.getSysUser().getUserType().equals(1)) {
                redisService.setCacheObject(CacheConstants.LOGIN_TOKEN_KEY_ + loginUser.getToken(), loginUser, MILLIS_MINUTE * timeout);
            }
        }
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
        JwtParser jwtParser = Jwts.parser().setSigningKey(jwtTokenProperties.getKey().getBytes());
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
            return JsonResult.ok().data(body);
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
        return getLoginUser(HttpContext.getRequest());

    }

    /**
     * 获取登录用户
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        String requestToken = SecurityUtils.getRequestToken(request, true);
        Claims claims = checkToken(requestToken);
        if (claims != null) {
            Integer userType = (Integer) claims.get("userType");
            if (userType.equals(1)) {
                LoginUser loginUser = redisService.getCacheObject(CacheConstants.LOGIN_TOKEN_KEY_ + requestToken);
                return loginUser;
            } else {
                LoginUser loginUser = new LoginUser();
                loginUser.setLoginTime(claims.getIssuedAt().getTime()).setExpireTime(claims.getExpiration().getTime());
                SysUser sysUser = new SysUser();
                sysUser.setId(((String) claims.get("userId")))
                        .setUserType(userType)
                        .setName(((String) claims.get("name")))
                        .setNickName(((String) claims.get("nickName")));
                loginUser.setSysUser(sysUser);
                return loginUser;
            }
        } else {
            // throw new IllegalAccessError("用户身份已过期");
            return null;
        }
    }

    /**
     * 删除登录用户
     */

}
