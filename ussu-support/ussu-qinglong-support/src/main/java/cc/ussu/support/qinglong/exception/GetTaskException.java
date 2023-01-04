package cc.ussu.support.qinglong.exception;

public class GetTaskException extends BaseQingLongException {
    public GetTaskException() {
        super("获取青龙任务失败");
    }

    public GetTaskException(String message) {
        super(message);
    }
}
