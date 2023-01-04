package cc.ussu.support.qinglong.exception;

public class SaveTaskException extends BaseQingLongException {

    public SaveTaskException() {
        super("保存任务失败");
    }

    public SaveTaskException(String message) {
        super(message);
    }
}
