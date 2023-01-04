package cc.ussu.support.qinglong.exception;

/**
 * 获取token异常
 */
public class GetTokenException extends BaseQingLongException {

    public GetTokenException() {
        super("获取青龙Token异常");
    }

    public GetTokenException(String message) {
        super(message);
    }
}
