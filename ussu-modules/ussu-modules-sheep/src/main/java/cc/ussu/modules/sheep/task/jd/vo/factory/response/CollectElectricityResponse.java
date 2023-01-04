package cc.ussu.modules.sheep.task.jd.vo.factory.response;

import lombok.Data;

/**
 * 收集电量
 */
public class CollectElectricityResponse extends JdFactoryBaseResponse<CollectElectricityResponse.CollectElectricityResult> {

    @Data
    public static class CollectElectricityResult {
        /**
         * 当前电池电量
         */
        private Integer batteryValue;
        /**
         * 收集电量
         */
        private Integer electricityValue;
    }

}
