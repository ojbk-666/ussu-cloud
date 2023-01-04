package cc.ussu.modules.sheep.task.jd.exception;

/**
 * 任务初始化失败
 */
public class TaskInitException extends RuntimeException {

    public TaskInitException() {
        this("任务初始化失败");
    }

    public TaskInitException(String message) {
        super(message);
    }
}
