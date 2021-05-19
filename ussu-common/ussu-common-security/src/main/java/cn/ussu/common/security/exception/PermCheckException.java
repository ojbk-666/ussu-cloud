package cn.ussu.common.security.exception;

import cn.ussu.common.core.constants.ErrorMsgConstants;

/**
 * 权限检查异常
 */
public class PermCheckException extends RuntimeException {

    public PermCheckException() {
        super(ErrorMsgConstants.UNAUTHORIZED);
    }

    public PermCheckException(String message) {
        super(message);
    }

}
