package cc.ussu.support.qinglong.exception;

public class DisableTaskException extends BaseQingLongException {
    public DisableTaskException() {
        super("禁用任务失败");
    }

    public DisableTaskException(String message) {
        super(message);
    }
}
