package cc.ussu.support.qinglong.exception;

public class RunTaskException extends BaseQingLongException {

    public RunTaskException() {
        super("运行任务失败");
    }

    public RunTaskException(String message) {
        super(message);
    }
}
