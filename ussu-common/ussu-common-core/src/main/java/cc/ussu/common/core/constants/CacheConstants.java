package cc.ussu.common.core.constants;

/**
 * 缓存中的所有key
 */
public interface CacheConstants extends RedisConstants {

    String TOKEN_IN_REQUEST_KEY = TokenConstants.AUTHENTICATION;
    String TOKEN_PREFIX = TokenConstants.PREFIX;
    /**
     * 登录用户 redis key
     */
    String LOGIN_TOKEN_KEY_ = "auth:login_tokens:";

    /**
     * 缓存有效期，默认720（分钟）
     */
    public final static long EXPIRATION = 720;

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public final static long REFRESH_TIME = 120;

    /**
     * 密码最大错误次数
     */
    public final static int PASSWORD_MAX_RETRY_COUNT = 5;

    /**
     * 密码锁定时间，默认10（分钟）
     */
    public final static long PASSWORD_LOCK_TIME = 10;


}
