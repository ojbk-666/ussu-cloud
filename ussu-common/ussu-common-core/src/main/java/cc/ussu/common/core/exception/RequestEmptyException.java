package cc.ussu.common.core.exception;

import cc.ussu.common.core.constants.ErrorCodeConstants;

/**
 * 业务异常的封装
 *
 * @author liming
 * @date 2019-09-25 20:03
 */
public class RequestEmptyException extends ServiceException {

	public RequestEmptyException() {
		super(ErrorCodeConstants.REQUEST_PARAM_ERROR, "请求数据不完整或格式错误！");
	}

	public RequestEmptyException(String errorMessage) {
		super(ErrorCodeConstants.REQUEST_PARAM_ERROR, errorMessage);
	}

	/**
	 * 不拷贝栈信息，提高性能
	 *
	 * @author fengshuonan
	 * @Date 2018/7/25 下午1:48
	 */
	@Override
	public synchronized Throwable fillInStackTrace() {
		return null;
	}
}
