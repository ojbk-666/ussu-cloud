package cn.ussu.common.security.handler;

import cn.ussu.common.core.constants.ErrorMsgConstants;
import cn.ussu.common.core.entity.JsonResult;
import cn.ussu.common.core.exception.UsernamePasswordInvalidException;
import cn.ussu.common.security.exception.PermCheckException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理
 */
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
     * 用户名或密码错误
     */
    @ExceptionHandler(UsernamePasswordInvalidException.class)
    public JsonResult handleUsernamePasswordInvalidException(UsernamePasswordInvalidException usernamePasswordInvalidException) {
        return JsonResult.error(usernamePasswordInvalidException.getErrorMessage());
    }

}
