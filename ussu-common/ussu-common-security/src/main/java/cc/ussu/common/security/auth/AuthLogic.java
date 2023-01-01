package cc.ussu.common.security.auth;

import cc.ussu.common.security.util.SecurityUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cc.ussu.common.core.exception.NotLoginException;
import cc.ussu.common.core.exception.NotPermissionException;
import cc.ussu.common.core.util.SecurityContextHolder;
import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.common.security.service.TokenService;
import cc.ussu.system.api.model.LoginUser;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

;

public class AuthLogic {

    public static final String ALL_PERMISSION = "*:*:*";
    public static final String SUPER_ADMIN = "super_admin";

    public TokenService tokenService = SpringUtil.getBean(TokenService.class);

    /**
     * 获取当前用户缓存信息, 如果未登录，则抛出异常
     *
     * @param token 前端传递的认证信息
     * @return 用户缓存信息
     */
    public LoginUser getLoginUser(String token) {
        return tokenService.getLoginUser(token);
    }

    /**
     * 获取当前用户缓存信息, 如果未登录，则抛出异常
     */
    public LoginUser getLoginUser() {
        String token = SecurityUtil.getRequestToken();
        if (token == null) {
            throw new NotLoginException("未提供token");
        }
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser == null) {
            throw new NotLoginException("无效的token");
        }
        return loginUser;
    }

    /**
     * 验证当前用户有效期, 如果相差不足120分钟，自动刷新缓存
     *
     * @param loginUser 当前用户信息
     */
    public void verifyLoginUserExpire(LoginUser loginUser) {
        tokenService.verifyToken(loginUser);
    }

    /**
     * 获取当前账号的权限列表
     */
    public Set<String> getPermList() {
        try {
            return getLoginUser().getPermissions();
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPerm(Collection<String> authorities, String permission) {
        return authorities.stream().filter(StrUtil::isNotBlank)
            .anyMatch(x -> ALL_PERMISSION.contains(x) || PatternMatchUtils.simpleMatch(x, permission));
    }

    /**
     * 判断是否包含角色
     *
     * @param roles 角色列表
     * @param role  角色
     * @return 用户是否具备某角色权限
     */
    public boolean hasRole(Collection<String> roles, String role) {
        return roles.stream().filter(StrUtil::isNotBlank)
            .anyMatch(x -> SUPER_ADMIN.contains(x) || PatternMatchUtils.simpleMatch(x, role));
    }

    /**
     * 根据注解(@PermCheck)鉴权, 如果验证未通过，则抛出异常: NotPermissionException
     */
    public void checkPerm(PermCheck permCheck) {
        SecurityContextHolder.setPermission(StrUtil.join(StrUtil.COMMA, permCheck.value()));
        // 超管检查
        if (SecurityUtil.isSuperAdmin()) {
            return;
        }
        if (permCheck.type() == PermCheck.PermCheckType.AND) {
            checkPermAnd(permCheck.value());
        } else {
            checkPermOr(permCheck.value());
        }
    }

    /**
     * 验证用户是否含有指定权限，只需包含其中一个
     */
    private void checkPermOr(String... permissions) {
        Set<String> permissionList = getPermList();
        for (String permission : permissions) {
            if (hasPerm(permissionList, permission)) {
                return;
            }
        }
        if (permissions.length > 0) {
            throw new NotPermissionException(permissions);
        }
    }

    /**
     * 验证用户是否含有指定权限，必须全部拥有
     */
    private void checkPermAnd(String... permissions) {
        Set<String> permissionList = getPermList();
        for (String permission : permissions) {
            if (!hasPerm(permissionList, permission)) {
                throw new NotPermissionException(permission);
            }
        }
    }

}
