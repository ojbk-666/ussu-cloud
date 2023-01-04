package cc.ussu.modules.sheep.task.jd.exception.fruit;

public class TaskInitForFarmException extends BaseFruitException {

    public TaskInitForFarmException() {
        super("获取农场任务列表异常");
    }

    public TaskInitForFarmException(String message) {
        super("获取农场任务列表异常:" + message);
    }

}
