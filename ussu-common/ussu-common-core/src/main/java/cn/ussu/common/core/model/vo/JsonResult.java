package cn.ussu.common.core.model.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.ussu.common.core.constants.StrConstants;

import java.util.HashMap;

/**
 * 常用返回结果,json返回通用对象
 *
 * @author liming
 * @date 2020-01-06 16:03
 */
public class JsonResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public static final int SUCCESS_CODE = StrConstants.SUCCESS_CODE;
    private static final int ERROR_CODE = StrConstants.ERROR_CODE;
    public static final String SUCCESS_MSG = StrConstants.ok;
    private static final String ERROR_MSG = StrConstants.error;

    private JsonResult() {
    }

    /*-----ok-----*/

    /**
     * 成功
     * code {@link #SUCCESS_MSG}<br>
     * msg {@link #SUCCESS_MSG}
     */
    public static JsonResult ok() {
        return ok(SUCCESS_CODE);
    }

    /**
     * 成功
     *
     * @param code code<br>
     *             msg {@link #SUCCESS_MSG}
     */
    public static JsonResult ok(int code) {
        return ok(code, SUCCESS_MSG);
    }

    /**
     * 成功
     *
     * @param successMsg 成功消息
     */
    public static JsonResult ok(String successMsg) {
        return ok(SUCCESS_CODE, successMsg);
    }

    /**
     * 成功
     *
     * @param code code
     * @param msg  msg
     */
    public static JsonResult ok(int code, String msg) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.put(StrConstants.code, code);
        jsonResult.put(StrConstants.msg, msg);
        return jsonResult;
    }

    /*-----error-----*/

    /**
     * 失败
     * code {@link #ERROR_CODE}
     * msg {@link #ERROR_MSG}
     */
    public static JsonResult error() {
        return error(ERROR_CODE);
    }

    /**
     * 失败
     * msg {@link #ERROR_MSG}
     *
     * @param code code
     */
    public static JsonResult error(int code) {
        return error(code, ERROR_MSG);
    }

    /**
     * 失败
     * code {@link #ERROR_CODE}
     *
     * @param msg msg
     */
    public static JsonResult error(String msg) {
        return error(ERROR_CODE, msg);
    }

    /**
     * 失败
     *
     * @param code code
     * @param msg  msg
     */
    public static JsonResult error(int code, String msg) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.put(StrConstants.code, code);
        jsonResult.put(StrConstants.msg, msg);
        return jsonResult;
    }

    /**
     * 设置消息
     *
     * @param msg
     * @return
     */
    public JsonResult setMsg(String msg) {
        super.put(StrConstants.msg, msg);
        return this;
    }

    /**
     * 获取消息
     */
    public String getMsg() {
        return ((String) get(StrConstants.msg));
    }

    /**
     * 放入指定的返回值
     *
     * @param key   key
     * @param value value
     */
    @Override
    public JsonResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 放入key为data的值
     *
     * @param data 值
     * @see #put(String, Object)
     */
    public JsonResult data(Object data) {
        put(StrConstants.data, data);
        return this;
    }

    public <T> T getData() {
        Object o = get(StrConstants.data);
        return ((T) o);
    }

    public <T> T getData(Class<T> clazz) {
        return BeanUtil.toBean(get(StrConstants.data), clazz);
    }

    /**
     * 放入key为form的值
     *
     * @param data 值
     * @see #put(String, Object)
     */
    public JsonResult setForm(Object data) {
        super.put("form", data);
        return this;
    }

    /**
     * 获取状态码
     */
    public int getCode() {
        return ((Integer) this.get(StrConstants.code));
    }

    public boolean isSuccess() {
        return SUCCESS_CODE == this.getCode();
    }
}
