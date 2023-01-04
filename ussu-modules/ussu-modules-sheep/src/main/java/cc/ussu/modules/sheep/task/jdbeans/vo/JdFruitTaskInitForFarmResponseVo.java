package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

/**
 * 东东农场
 */
@Data
public class JdFruitTaskInitForFarmResponseVo {

    private String code;
    private String message;
    private Long sysTime;
    private TotalWaterTaskInit totalWaterTaskInit;
    private Boolean allTaskFinished;

    @Data
    public class TotalWaterTaskInit {
        private Integer totalWaterTaskLimit;
        private Integer totalWaterTaskEnergy;
        private Boolean f;
        private Boolean totalWaterTaskFinished;
        private Integer totalWaterTaskTimes;
    }

}
