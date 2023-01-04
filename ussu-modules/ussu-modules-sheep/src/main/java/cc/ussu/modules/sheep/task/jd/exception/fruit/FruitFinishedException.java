package cc.ussu.modules.sheep.task.jd.exception.fruit;

import lombok.Data;

/**
 * 水果已成熟
 */
@Data
public class FruitFinishedException extends Exception {

    public FruitFinishedException() {
        super("水果已成熟");
    }

    public FruitFinishedException(String message) {
        super(message);
    }
}
