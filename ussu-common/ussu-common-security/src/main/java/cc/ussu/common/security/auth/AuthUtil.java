package cc.ussu.common.security.auth;

import cc.ussu.common.security.annotation.PermCheck;
import cc.ussu.system.api.model.LoginUser;

public class AuthUtil {

    public static AuthLogic authLogic = new AuthLogic();

    /**
     * 获取当前登录用户信息
     */
    public static LoginUser getLoginUser(String token) {
        return authLogic.getLoginUser(token);
    }

    /**
     * 验证当前用户有效期
     */
    public static void verifyLoginUserExpire(LoginUser loginUser) {
        authLogic.verifyLoginUserExpire(loginUser);
    }

    /**
     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotPermissionException
     */
    public static void checkPerm(PermCheck permCheck) {
        authLogic.checkPerm(permCheck);
    }

}
