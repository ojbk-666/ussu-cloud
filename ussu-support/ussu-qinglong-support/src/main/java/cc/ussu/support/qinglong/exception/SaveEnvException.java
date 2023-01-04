package cc.ussu.support.qinglong.exception;

public class SaveEnvException extends BaseQingLongException {

    public SaveEnvException() {
        super("保存环境变量失败");
    }

    public SaveEnvException(String message) {
        super(message);
    }
}
