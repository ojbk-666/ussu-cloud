package cc.ussu.common.core.constants;

/**
 * 系统参数key
 */
public interface ParamKeyConstants {
	/**
	 * 默认密码
	 */
	String DEFAULT_PWD = "ussu_sys_default_pwd";
	/**
	 * 敏感操作口令
	 */
	String SENSITIVE_OPERATE_CODE = "ussu_sys_sensitive_operate_code";
	/**
	 * 验证码类型
	 * 1 静态png
	 * 2 静态中文
	 * 3 静态算术
	 * 4 动态gif
	 * 5 动态中文
	 */
	String CAPTCHA_TYPE = "ussu_sys_captcha_code_type";

	/**
	 * 系统名称
	 */
	String SYS_NAME = "ussu_sys_name";

	/**
	 * 验证码登录
	 */
	String VERCODE_LOGIN = "ussu_vercode_login";

	/**
	 * 静态资源
	 */
	String STATIC_LOCATION = "ussu_sys_static_location";

	/**
	 * 图片服务器地址
	 */
	String IMAGE_SERVER = "ussu_image_server";

	/**
	 * 自助注册
	 */
	String SYS_SELF_REG = "ussu_sys_self_reg";

	/**
	 * 显示消息
	 */
	String SHOW_MESSAGE = "ussu_admin_show_message";

	/**
	 * 显示便签
	 */
	String SHOW_NOTE = "ussu_admin_show_note";

	/**
	 * 显示主题设置
	 */
	String SHOW_EXT_THEME = "ussu_admin_show_ext_theme";

	/**
	 * 显示框架扩展配置
	 */
	String SHOW_EXT_CONFIG = "ussu_admin_show_ext_config";

}
