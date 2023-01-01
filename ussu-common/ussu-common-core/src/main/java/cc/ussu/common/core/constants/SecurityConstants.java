package cc.ussu.common.core.constants;

/**
 * 权限相关通用常量
 */
public interface SecurityConstants {
    /**
     * 用户ID字段
     */
    String DETAILS_USER_ID = "user_id";

    /**
     * 用户名字段
     */
    String DETAILS_USERNAME = "account";

    /**
     * 授权信息字段
     */
    String AUTHORIZATION_HEADER = "authorization";

    /**
     * 请求来源
     */
    String FROM_SOURCE = "from-source";

    /**
     * 内部请求
     */
    String INNER = "inner";

    /**
     * 用户标识
     */
    String USER_KEY = "user_key";

    /**
     * 登录用户
     */
    String LOGIN_USER = "login_user";

    /**
     * 角色权限
     */
    String ROLE_PERMISSION = "role_permission";

    /**
     * traceId
     */
    String TRACE_ID = "trace_id";
}
