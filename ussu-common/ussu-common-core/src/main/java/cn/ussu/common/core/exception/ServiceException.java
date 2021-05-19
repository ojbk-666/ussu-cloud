package cn.ussu.common.core.exception;

/**
 * 业务异常的封装 guns
 *
 * @author liming
 * @date 2019-09-25 20:01
 */
public class ServiceException extends RuntimeException {

	private Integer code;

	private String errorMessage;

	public ServiceException(Integer code, String errorMessage) {
		super(errorMessage);
		this.code = code;
		this.errorMessage = errorMessage;
	}

	public ServiceException(AbstractBaseExceptionEnum exception) {
		super(exception.getMessage());
		this.code = exception.getCode();
		this.errorMessage = exception.getMessage();
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
