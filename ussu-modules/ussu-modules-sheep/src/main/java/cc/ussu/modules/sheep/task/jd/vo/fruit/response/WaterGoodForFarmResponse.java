package cc.ussu.modules.sheep.task.jd.vo.fruit.response;

import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import lombok.Data;

@Data
public class WaterGoodForFarmResponse extends JdBaseResponse {

    /**
     * 总水滴数
     */
    private Integer totalEnergy;
    private String statisticsTimes;
    /**
     * 浇水次数
     */
    private Integer totalWaterTimes;
    private Long sysTime;
    private Boolean finished;
    private Integer waterStatus;
    /**
     * 浇水水滴数
     */
    private Integer sendAmount;
    /**
     * 果树总水滴数
     */
    private Integer treeEnergy;

}
