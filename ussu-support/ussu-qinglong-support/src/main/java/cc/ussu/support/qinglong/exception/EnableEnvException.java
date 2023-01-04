package cc.ussu.support.qinglong.exception;

public class EnableEnvException extends BaseQingLongException {
    public EnableEnvException() {
        super("启用环境变量失败");
    }

    public EnableEnvException(String message) {
        super(message);
    }
}
