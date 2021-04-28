package cn.ussu.common.core.constants;

/**
 * 缓存中的所有key
 */
public interface CacheConstants {

    String TOKEN_IN_REQUEST_KEY = "token";
    String TOKEN_PREFIX = "Bearer ";
    /**
     * 登录用户 redis key
     */
    String LOGIN_TOKEN_KEY_ = "auth:login_tokens:";

}
