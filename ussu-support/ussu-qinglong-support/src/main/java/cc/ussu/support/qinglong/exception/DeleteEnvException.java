package cc.ussu.support.qinglong.exception;

public class DeleteEnvException extends BaseQingLongException {

    public DeleteEnvException() {
        super("删除环境变量失败");
    }

    public DeleteEnvException(String message) {
        super(message);
    }
}
