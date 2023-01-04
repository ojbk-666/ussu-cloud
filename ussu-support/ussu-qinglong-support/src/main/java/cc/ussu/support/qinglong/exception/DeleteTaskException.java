package cc.ussu.support.qinglong.exception;

public class DeleteTaskException extends BaseQingLongException {

    public DeleteTaskException() {
        super("删除任务失败");
    }

    public DeleteTaskException(String message) {
        super(message);
    }
}
