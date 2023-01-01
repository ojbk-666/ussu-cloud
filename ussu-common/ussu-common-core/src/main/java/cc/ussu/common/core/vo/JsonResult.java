package cc.ussu.common.core.vo;

import cc.ussu.common.core.constants.SecurityConstants;
import cc.ussu.common.core.constants.StrConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.MDC;

import java.io.Serializable;

/**
 * 常用返回结果,json返回通用对象
 *
 * @author liming
 * @date 2020-01-06 16:03
 */
@Setter
@Getter
@Accessors(chain = true)
public class JsonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int SUCCESS_CODE = StrConstants.SUCCESS_CODE;
    public static final int ERROR_CODE = StrConstants.ERROR_CODE;
    public static final String DEFAULT_SUCCESS_MSG = StrConstants.ok;
    public static final String DEFAULT_ERROR_MSG = StrConstants.error;

    protected int code;
    protected String msg;
    protected String traceId = MDC.get(SecurityConstants.TRACE_ID);
    protected T data;

    public JsonResult() {
    }

    private static <T> JsonResult<T> restResult(T data, int code, String msg) {
        JsonResult<T> result = new JsonResult<>();
        result.code = code;
        result.msg = msg;
        result.data = data;
        return result;
    }

    /*-----ok-----*/

    /**
     * 成功
     * code {@link #DEFAULT_SUCCESS_MSG}<br>
     * msg {@link #DEFAULT_SUCCESS_MSG}
     */
    public static <T> JsonResult<T> ok() {
        return restResult(null, SUCCESS_CODE, null);
    }

    /**
     * 成功
     *
     * @param data 成功响应
     */
    public static <T> JsonResult<T> ok(T data) {
        return restResult(data, SUCCESS_CODE, null);
    }

    /**
     * 成功
     *
     * @param data data
     * @param msg  msg
     */
    public static <T> JsonResult<T> ok(T data, String msg) {
        return restResult(data, SUCCESS_CODE, msg);
    }

    /*-----error-----*/

    /**
     * 失败
     * code {@link #ERROR_CODE}
     * msg {@link #DEFAULT_ERROR_MSG}
     */
    public static <T> JsonResult<T> error() {
        return restResult(null, ERROR_CODE, null);
    }

    /**
     * 失败
     * msg {@link #DEFAULT_ERROR_MSG}
     *
     * @param code code
     */
    public static <T> JsonResult<T> error(int code) {
        return restResult(null, code, null);
    }

    /**
     * 失败
     * code {@link #ERROR_CODE}
     *
     * @param msg msg
     */
    public static <T> JsonResult<T> error(String msg) {
        return restResult(null, ERROR_CODE, msg);
    }

    /**
     * 失败
     *
     * @param code code
     * @param msg  msg
     */
    public static <T> JsonResult<T> error(int code, String msg) {
        return restResult(null, code, msg);
    }

    /**
     * 失败
     */
    public static <T> JsonResult<T> error(T data, String msg) {
        return restResult(data, ERROR_CODE, msg);
    }

    public boolean isOk() {
        return SUCCESS_CODE == this.code;
    }

    public boolean isError() {
        return SUCCESS_CODE != this.code;
    }
}
