package cc.ussu.modules.sheep.task.jd.exception;

public class JdCookieExpiredException extends RuntimeException {

    public JdCookieExpiredException() {
        super("ck失效");
    }

    public JdCookieExpiredException(String msg) {
        super(msg);
    }
}
