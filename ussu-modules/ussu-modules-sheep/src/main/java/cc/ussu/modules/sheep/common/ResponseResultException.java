package cc.ussu.modules.sheep.common;

/**
 * 响应获取的结果异常
 */
public class ResponseResultException extends RuntimeException {

    public ResponseResultException() {
        super("请求成功，结果异常");
    }

    public ResponseResultException(String message) {
        super("请求成功，结果异常：" + message);
    }
}
