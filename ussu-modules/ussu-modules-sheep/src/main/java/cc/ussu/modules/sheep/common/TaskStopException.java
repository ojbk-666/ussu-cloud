package cc.ussu.modules.sheep.common;

/**
 * 任务被停止
 */
public class TaskStopException extends InterruptedException {

    public TaskStopException() {
        super("任务被停止");
    }
}
