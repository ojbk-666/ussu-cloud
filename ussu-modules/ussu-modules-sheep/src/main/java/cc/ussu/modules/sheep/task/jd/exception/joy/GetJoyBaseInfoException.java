package cc.ussu.modules.sheep.task.jd.exception.joy;

public class GetJoyBaseInfoException extends RuntimeException {

    public GetJoyBaseInfoException() {
        this("获取joy基础信息失败");
    }

    public GetJoyBaseInfoException(String message) {
        super(message);
    }
}
