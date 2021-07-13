package cn.ussu.common.security.handler;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.ussu.common.core.constants.ErrorMsgConstants;
import cn.ussu.common.core.exception.UsernamePasswordInvalidException;
import cn.ussu.common.core.model.vo.JsonResult;
import cn.ussu.common.security.exception.PermCheckException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

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

    /**
     * 断言异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public JsonResult handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return JsonResult.error(illegalArgumentException.getMessage());
    }

    /**
     * sql异常
     */
    @ExceptionHandler(SQLException.class)
    public JsonResult handleSQLException(SQLException sqlException) {
        return JsonResult.error(sqlException.getMessage()).put("sqlState", sqlException.getSQLState());
    }

    /**
     * 其他运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public JsonResult handleRuntimeException(RuntimeException runtimeException) {
        runtimeException.printStackTrace();
        return JsonResult.error(runtimeException.getMessage()).put("exception", ExceptionUtil.stacktraceToString(runtimeException));
    }

}
