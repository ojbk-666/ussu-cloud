package cc.ussu.support.qinglong.exception;

public class GetEnvException extends BaseQingLongException {

    public GetEnvException() {
        super("获取青龙环境变量异常");
    }

    public GetEnvException(String message) {
        super(message);
    }
}
