package cc.ussu.modules.sheep.task.jd.vo.speedcoin;

import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@Accessors(chain = true)
public class JdSpeedCoinThreadLocalDTO {

    private JdCookieVO jdCookieVO;

    private Boolean canStartNewItem;

    private Boolean llAPIError;

    /**
     * 本地运行获取多少金币
     */
    private AtomicInteger score = new AtomicInteger(0);

}
