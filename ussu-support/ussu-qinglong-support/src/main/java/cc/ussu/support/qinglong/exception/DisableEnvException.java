package cc.ussu.support.qinglong.exception;

public class DisableEnvException extends BaseQingLongException {

    public DisableEnvException() {
        super("禁用环境变量失败");
    }

    public DisableEnvException(String message) {
        super(message);
    }
}
