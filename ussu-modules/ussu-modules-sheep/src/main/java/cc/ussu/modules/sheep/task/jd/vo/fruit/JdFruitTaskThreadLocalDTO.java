package cc.ussu.modules.sheep.task.jd.vo.fruit;

import cc.ussu.modules.sheep.task.jd.vo.JdCookieVO;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.InitForFarmResponse;
import cc.ussu.modules.sheep.task.jd.vo.fruit.response.TaskInitForFarmResponse;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用于任务临时保存的变量
 */
@Data
@Accessors(chain = true)
public class JdFruitTaskThreadLocalDTO {

    /**
     * ck
     */
    private volatile JdCookieVO jdCookieVO;

    /**
     * 农场信息
     */
    private volatile InitForFarmResponse initForFarmResponse;

    /**
     * 任务列表
     */
    private volatile TaskInitForFarmResponse taskInitForFarmResponse;

}
