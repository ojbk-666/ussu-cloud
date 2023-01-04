package cc.ussu.modules.sheep.task.jd.exception.wskey;

public class JdWsKeyExpiredException extends RuntimeException {

    public JdWsKeyExpiredException() {
        super("wskey已过期");
    }
}
