package cc.ussu.modules.dczx.exception;

/**
 * 任务被暂停
 */
public class TaskPausedException extends RuntimeException{

    public TaskPausedException() {
        super("任务被暂停");
    }
}
