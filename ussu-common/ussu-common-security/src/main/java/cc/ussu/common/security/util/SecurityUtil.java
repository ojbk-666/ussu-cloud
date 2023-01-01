package cc.ussu.common.security.util;

import cc.ussu.common.core.constants.CacheConstants;
import cc.ussu.common.core.constants.SecurityConstants;
import cc.ussu.common.core.constants.TokenConstants;
import cc.ussu.common.core.util.HttpContextHolder;
import cc.ussu.common.core.util.SecurityContextHolder;
import cc.ussu.system.api.model.LoginUser;
import cc.ussu.system.api.vo.SysUserVO;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class SecurityUtil {

    /**
     * 获取用户id
     */
    public static String getUserId() {
        /*LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return null;
        }
        return loginUser.getUser().getId();*/
        return SecurityContextHolder.getUserId();
    }

    /**
     * 获取用户名
     */
    public static String getUsername() {
        /*LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return null;
        }
        return loginUser.getAccount();*/
        return SecurityContextHolder.getUserName();
    }

    /**
     * 获取用户账号
     */
    public static String getAccount() {
        /*LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return null;
        }
        return loginUser.getUser().getAccount();*/
        return SecurityContextHolder.getUserName();
    }

    /**
     * 获取用户key
     */
    public static String getUserKey() {
        return SecurityContextHolder.getUserKey();
    }

    /**
     * 获取用户信息
     */
    /*public static LoginUser getLoginUser() {
        TokenService tokenService = SpringUtil.getBean(TokenService.class);
        LoginUser loginUser = tokenService.getLoginUser();
        return loginUser;
    }*/

    /**
     * 获取登录用户信息
     */
    public static LoginUser getLoginUser() {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
    }

    /**
     * 获取登录用户信息
     */
    public static SysUserVO getLoginUserSysUser() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return null;
        }
        return loginUser.getUser();
    }

    /**
     * 获取请求token
     */
    public static String getRequestToken(HttpServletRequest request) {
        return getRequestToken(request, true);
    }

    /**
     * 获取请求token
     */
    public static String getRequestToken(HttpServletRequest request, boolean removePrefix) {
        String requestToken = request.getHeader(TokenConstants.AUTHENTICATION);
        if (StrUtil.isBlank(requestToken)) {
            Cookie[] cookies = request.getCookies();
            if (ArrayUtil.isNotEmpty(cookies)) {
                for (Cookie cookie : cookies) {
                    if (TokenConstants.AUTHENTICATION.equals(cookie.getName())) {
                        String cookieValue = cookie.getValue();
                        if (StrUtil.isNotBlank(cookieValue)) {
                            requestToken = cookieValue;
                        }
                        break;
                    }
                }
            }
        }
        if (StrUtil.isNotBlank(requestToken)) {
            requestToken = URLUtil.decode(requestToken);
            if (removePrefix && requestToken.startsWith(CacheConstants.TOKEN_PREFIX)) {
                requestToken = requestToken.replaceFirst(CacheConstants.TOKEN_PREFIX, StrUtil.EMPTY);
            }
        }
        return requestToken;
    }

    /**
     * 获取请求token（当前请求上下文）
     */
    public static String getRequestToken() {
       return getRequestToken(HttpContextHolder.getRequest());
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

    public static String getSuperAdminUserId() {
        return "super";
    }

    /**
     * 当前用户是否为超管
     */
    public static boolean isSuperAdmin() {
        return isSuperAdmin(getUserId());
    }

    public static boolean isSuperAdmin(String userId) {
        return getSuperAdminUserId().equals(userId);
    }

    /**
     * 是否不是超管
     */
    public static boolean isNotSuperAdmin(String userId) {
        return !isSuperAdmin(userId);
    }

    /**
     * 当前用户是否不是超管
     */
    public static boolean isNotSuperAdmin() {
        return !isSuperAdmin();
    }

}
