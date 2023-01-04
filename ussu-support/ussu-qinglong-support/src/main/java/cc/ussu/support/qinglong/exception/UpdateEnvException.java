package cc.ussu.support.qinglong.exception;

public class UpdateEnvException extends BaseQingLongException {

    public UpdateEnvException() {
        super("更新环境变量失败");
    }

    public UpdateEnvException(String message) {
        super(message);
    }
}
