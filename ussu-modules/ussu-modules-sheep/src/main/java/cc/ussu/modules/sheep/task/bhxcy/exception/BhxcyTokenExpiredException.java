package cc.ussu.modules.sheep.task.bhxcy.exception;

public class BhxcyTokenExpiredException extends RuntimeException {

    public BhxcyTokenExpiredException() {
        super("登录信息已失效，请重新抓取token");
    }

    public BhxcyTokenExpiredException(String message) {
        super(message);
    }
}
