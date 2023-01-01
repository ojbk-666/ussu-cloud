package cc.ussu.common.core.constants;

/**
 * 错误消息
 *
 * @author liming
 * @date 2019-09-25 20:12
 */
public interface ErrorMsgConstants {

	String SERVER_ERROR = "服务器错误";

	String TIME_OUT = "会话超时";

	String REQUEST_PARAM_ERROR = "请求参数错误";

	String UNAUTHORIZED = "没有操作权限";

	String ACCOUNT_FORCE_LOGOUT = "账号被强制下线";

	String DEMO_MODE = "演示模式,不允许操作";

	String DATA_INVALID = "数据已被他人修改，请刷新";

	String ACCOUNT_UNKNOWN = "用户名不存在";

	String ACCOUNT_LOCKED = "账号被锁定";

	String ACCOUNT_DISABLED = "账号被禁用";

	String PASSWORD_ERROR = "密码错误";

	String ACC_PWD_ERROR = "用户名或密码错误";

	String GRAPH_CAPTCHA = "图形验证码错误";

}
