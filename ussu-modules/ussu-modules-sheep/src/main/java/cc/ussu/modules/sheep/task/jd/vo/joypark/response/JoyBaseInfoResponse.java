package cc.ussu.modules.sheep.task.jd.vo.joypark.response;

import cc.ussu.modules.sheep.task.jd.vo.joypark.JdJoyBaseResponse;
import lombok.Data;

@Data
public class JoyBaseInfoResponse extends JdJoyBaseResponse {

    private JoyBaseInfo data;

    @Data
    public static class JoyBaseInfo {
        private int fastBuyCoin;

        private Integer helpState;

        private Long serverTime;

        private Integer joyCoin;

        private Integer loopTimes;

        private Integer bigRewardState;

        private String invitePin;

        private String leaveJoyCoin;

        private Integer hasStartCoin;

        private String speed;

        private String helpType;

        private Integer startCoin;

        /**
         * 快速购买的joy等级
         */
        private Integer fastBuyLevel;

        private String activityId;

        private String forwardUrl;

        private Integer guideStep;

        private Integer upSpeed;
        /**
         * 等级
         */
        private Integer level;
        private String userIcon;
        private String maxReward;
        private Integer workPlaceUnlock;
        private String orderCancel;
        private String orderCancelCoin;
        private Integer scene;
    }

}
