package cn.ussu.common.security.exception;

/**
 * 权限检查异常
 */
public class PermCheckException extends RuntimeException {

    public PermCheckException() {
        super("没有访问权限");
    }

    public PermCheckException(String message) {
        super(message);
    }

}
