package cc.ussu.support.qinglong.exception;

public class UpdateTaskException extends BaseQingLongException {
    public UpdateTaskException() {
        super("更新任务失败");
    }

    public UpdateTaskException(String message) {
        super(message);
    }
}
