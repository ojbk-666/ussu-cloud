package cc.ussu.support.qinglong.exception;

public class EnableTaskException extends BaseQingLongException {

    public EnableTaskException() {
        super("启用任务成功");
    }

    public EnableTaskException(String message) {
        super(message);
    }
}
