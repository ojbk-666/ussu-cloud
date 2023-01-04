package cc.ussu.modules.sheep.task.jd.exception.joy;

public class GameShopListException extends RuntimeException{

    public GameShopListException() {
        this("获取可购买的joy失败");
    }

    public GameShopListException(String message) {
        super(message);
    }
}
