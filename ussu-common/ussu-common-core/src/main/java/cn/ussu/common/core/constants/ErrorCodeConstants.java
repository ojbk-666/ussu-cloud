package cn.ussu.common.core.constants;

/**
 * 错误码
 *
 * @author liming
 * @date 2019-10-12 12:16:38
 */
public interface ErrorCodeConstants {
	/**
	 * 用户名或密码错误
	 */
	int ACC_PWD_ERROR = StrConstants.ERROR_CODE;
	/**
	 * 服务器错误
	 */
	int SERVER_ERROR = 500;
	/**
	 * 请求参数错误
	 */
	int REQUEST_PARAM_ERROR = 400;
	/**
	 * 会话超时
	 */
	int TIME_OUT = 50014;
	/**
	 * 没有操作权限
	 */
	int UNAUTHORIZED = 403;
	/**
	 * 页面未找到
	 */
	int PAGE_NOT_FOUND = 404;
	/**
	 * 账号被强制下线
	 */
	int ACCOUNT_FORCE_LOGOUT = -201;
	/**
	 * 账号被禁用
	 */
	int ACCOUNT_DISABLED = -203;
	/**
	 * 演示模式
	 */
	int DEMO_MODE = -1011;
	/**
	 * 数据已被他人修改
	 */
	int DATA_INVALID = 701;

}
