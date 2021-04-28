package cn.ussu.common.security.handler;

import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.security.exception.PermCheckException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PermCheckException.class)
    public JsonResult handlePermCheckException(PermCheckException permCheckException) {
        return JsonResult.error("没有访问权限,请联系管理员");
    }

}
