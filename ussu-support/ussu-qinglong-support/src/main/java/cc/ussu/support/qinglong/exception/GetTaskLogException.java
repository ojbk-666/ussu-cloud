package cc.ussu.support.qinglong.exception;

public class GetTaskLogException extends BaseQingLongException {

    public GetTaskLogException() {
        super("获取青龙任务日志失败");
    }

    public GetTaskLogException(String message) {
        super(message);
    }

}
