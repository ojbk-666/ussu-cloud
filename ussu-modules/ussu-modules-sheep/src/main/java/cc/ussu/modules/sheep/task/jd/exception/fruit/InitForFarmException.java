package cc.ussu.modules.sheep.task.jd.exception.fruit;

public class InitForFarmException extends BaseFruitException {

    public InitForFarmException() {
        super("初始化农场数据异常");
    }

    public InitForFarmException(String message) {
        super(message);
    }
}
