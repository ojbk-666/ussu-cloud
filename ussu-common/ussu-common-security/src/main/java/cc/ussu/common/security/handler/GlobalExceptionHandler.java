package cc.ussu.common.security.handler;

import cc.ussu.common.core.exception.NotPermissionException;
import cc.ussu.common.core.exception.UsernamePasswordInvalidException;
import cc.ussu.common.security.exception.PermCheckException;
import cn.hutool.core.exceptions.ExceptionUtil;
import cc.ussu.common.core.constants.ErrorMsgConstants;
import cc.ussu.common.core.vo.JsonResult;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * 异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 权限不足
     */
    @ExceptionHandler(PermCheckException.class)
    public JsonResult handlePermCheckException(PermCheckException permCheckException) {
        return JsonResult.error(ErrorMsgConstants.UNAUTHORIZED);
    }

    /**
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public JsonResult handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限码校验失败'{}'", requestURI, e.getMessage());
        return JsonResult.error(HttpStatus.FORBIDDEN, "没有访问权限，请联系管理员授权");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public JsonResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return JsonResult.error(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public JsonResult handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return JsonResult.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return JsonResult.error(message);
    }

    /**
     * todo 用户名或密码错误
     */
    @ExceptionHandler(UsernamePasswordInvalidException.class)
    public JsonResult handleUsernamePasswordInvalidException(UsernamePasswordInvalidException usernamePasswordInvalidException) {
        return JsonResult.error(usernamePasswordInvalidException.getErrorMessage());
    }

    /**
     * todo 断言异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public JsonResult handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return JsonResult.error(illegalArgumentException.getMessage());
    }

    /**
     * sql异常
     */
    @ExceptionHandler(SQLException.class)
    public JsonResult handleSQLException(SQLException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生SQL异常.", request.getRequestURI(), e);
        return JsonResult.error(e.getSQLState(), e.getMessage());
    }

    @ExceptionHandler(FeignException.FeignServerException.class)
    public JsonResult handlerFeignServerException(FeignException.FeignServerException e, HttpServletRequest request) {
        log.error("请求地址:{},feignServer异常", request.getRequestURI(), e);
        return JsonResult.error(e.status(), e.getMessage());
    }

    @ExceptionHandler(FeignException.FeignClientException.class)
    public JsonResult handlerFeignServerException(FeignException.FeignClientException e, HttpServletRequest request) {
        log.error("请求地址:{},feignClient异常", request.getRequestURI(), e);
        return JsonResult.error(e.status(), e.getMessage());
    }

    /**
     * 其他运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public JsonResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生未知异常.", request.getRequestURI(), e);
        return JsonResult.error(ExceptionUtil.stacktraceToString(e), e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public JsonResult handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return JsonResult.error(e.getMessage());
    }

}
