package cn.ussu.common.core.constants;

public interface RedisConstants {

    /**
     * 系统参数
     */
    String SYS_PARAM = "sys:param:sys_param";
    /**
     * 字典
     */
    String SYS_DICT_ = "sys:dict:";
    /**
     * 区域
     */
    String SYS_AREA_ = "sys:sys-area:";
    /**
     * 权限
     */
    String SYS_PERM = "sys:perm:sys_perm";
    /**
     * 角色权限
     */
    String SYS_ROLE_PERM_ = "sys:role_perm:";
    /**
     * 用户信息
     */
    String SYS_USERINFO = "sys:user_info";
    /**
     * token
     */
    String SYS_TOKEN = "sys:token";
    /**
     * 登录用户 redis key
     */
    String LOGIN_TOKEN_KEY_ = "sys:login_tokens:";

    /**
     * 令牌前缀
     */
    String LOGIN_USER_KEY = "login_user_key";

    /**
     * 登录验证码 redis key
     */
    String LOGIN_UUID_CAPTCHA_ = "sys:login_uuid_captcha:";

    /**
     * 附件id-附件路径
     */
    String SYS_ATTACH_ID_PATH_ = "sys:attach:id_path:";
}
