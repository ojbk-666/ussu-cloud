package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response;

import cc.ussu.modules.sheep.task.jd.vo.speedsignfree.JdSpeedBaseResponse;
import lombok.Data;

@Data
public class WheelsLotteryResponse extends JdSpeedBaseResponse {

    private WheelsLotteryData data;

    @Data
    public static class WheelsLotteryData {
        private Integer rewardType;
        private String prizeCode;
        private Integer lotteryChances;
        private String lotteryTime;
        private String rewardId;
        private Boolean expired;
        private Integer couponUsedValue;
        private Integer rewardValue;
        private String couponDesc;
    }

}
