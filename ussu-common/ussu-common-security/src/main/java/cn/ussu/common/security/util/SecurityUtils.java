package cn.ussu.common.security.util;

import cn.hutool.core.util.StrUtil;
import cn.ussu.common.core.constants.CacheConstants;
import cn.ussu.common.security.entity.LoginUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;

public class SecurityUtils {

    /**
     * 获取用户名
     */
    public static String getUsername() {
        return null;
    }

    /**
     * 获取用户id
     */
    public static String getUserId() {
        return null;
    }

    /**
     * 获取用户账号
     */
    public static String getUserAccount() {
        return null;
    }

    /**
     * 获取用户信息
     */
    public static LoginUser getLoginUser() {
        return null;
    }

    /**
     * 获取请求token
     */
    public static String getRequestToken(HttpServletRequest request) {
        String requestToken = request.getHeader(CacheConstants.TOKEN_IN_REQUEST_KEY);
        if (StrUtil.isNotBlank(requestToken) && requestToken.startsWith(CacheConstants.TOKEN_PREFIX)) {
            requestToken = requestToken.replaceFirst(CacheConstants.TOKEN_PREFIX, StrUtil.EMPTY);
        }
        return requestToken;
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 当前用户是否为超管
     *
     * @return 结果
     */
    public static boolean isSuperAdmin() {
        // return SpringContextHolder.getBean(UssuProperties.class).getSuperAdmin().equals(getLoginUser().getUser().getId());
        return true;
    }

}
