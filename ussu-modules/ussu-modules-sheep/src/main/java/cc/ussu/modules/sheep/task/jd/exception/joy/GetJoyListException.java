package cc.ussu.modules.sheep.task.jd.exception.joy;

public class GetJoyListException extends RuntimeException{

    public GetJoyListException() {
        this("获取已有的joy列表失败");
    }

    public GetJoyListException(String message) {
        super(message);
    }
}
