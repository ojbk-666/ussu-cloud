package cn.ussu.gateway.util;

import cn.ussu.common.core.constants.CacheConstants;
import cn.ussu.common.core.constants.ErrorCodeConstants;
import cn.ussu.common.core.constants.ErrorMsgConstants;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.core.util.SpringContextHolder;
import cn.ussu.gateway.properties.JwtTokenProperties;
import io.jsonwebtoken.*;

public class TokenUtil {

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
    protected static Claims parseToken(String tokenStr) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        if (tokenStr.startsWith(CacheConstants.TOKEN_PREFIX)) {
            tokenStr = tokenStr.substring(CacheConstants.TOKEN_PREFIX.length());
        }
        JwtTokenProperties jwtTokenProperties = SpringContextHolder.getBean(JwtTokenProperties.class);
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
    public static Claims checkToken(String tokenStr) {
        try {
            return parseToken(tokenStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取token检查结果
     *
     * @param tokenStr
     * @return
     */
    public static JsonResult checkTokenResult(String tokenStr) {
        try {
            Claims body = parseToken(tokenStr);
            return JsonResult.ok().data(body);
        } catch (ExpiredJwtException e) {
            return JsonResult.error(ErrorCodeConstants.TIME_OUT, ErrorMsgConstants.TIME_OUT);
        } catch (Exception e) {
            return JsonResult.error(ErrorCodeConstants.TIME_OUT, ErrorMsgConstants.TIME_OUT);
        }
    }

}
