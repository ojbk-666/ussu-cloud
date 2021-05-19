package cn.ussu.common.core.exception;

/**
 * roses异常规范
 *
 * @author liming
 * @date 2019-09-25 20:02
 */
public interface AbstractBaseExceptionEnum {

	/**
	 * 获取异常的状态码
	 */
	Integer getCode();

	/**
	 * 获取异常的提示信息
	 */
	String getMessage();

}
