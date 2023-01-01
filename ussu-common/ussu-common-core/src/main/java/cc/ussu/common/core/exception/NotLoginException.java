package cc.ussu.common.core.exception;

/**
 * 未登录异常
 */
public class NotLoginException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotLoginException(String message) {
        super(message);
    }

}
