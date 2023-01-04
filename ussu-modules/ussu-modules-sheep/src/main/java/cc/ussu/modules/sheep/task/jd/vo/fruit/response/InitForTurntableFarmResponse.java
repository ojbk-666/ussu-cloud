package cc.ussu.modules.sheep.task.jd.vo.fruit.response;

import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class InitForTurntableFarmResponse extends JdBaseResponse {

    private String shareCodeAddOn;
    private List<String> userFriendInfoDtos;
    private Integer helpedGotLotteryTimes;
    private Integer masterHelpTimes;
    private List<TurntableInfo> turntableInfos;
    private Integer addHelpUsers;
    private Boolean timingGotStatus;
    private Long timingLastSysTime;
    private Boolean firstEnter;
    private List<TurntableBrowserAd> turntableBrowserAds;
    private String statisticsTimes;
    private Integer timingIntervalHours;
    private Integer callUserGotLotteryTimes;
    private Integer callUserAddWater;
    private Long sysTime;
    private Integer timingLotteryTimes;
    private Integer helpedTimesByOther;
    private List<String> masterHelpUsers;
    /**
     * 天天抽奖次数
     */
    private int remainLotteryTimes;
    private String callUserSendFruit;
    private Boolean callUser;

    @Data
    public static class TurntableInfo {
        /**
         * 抽中的奖品类型 bean1 coupon1 thanks
         */
        private String type;
        private String icon;
        /**
         * 奖品名称
         */
        private String name;
        private Integer totalLimit;
    }

    @Data
    public static class TurntableBrowserAd {

        private String adId;
        private String icon;
        private String main;
        private String sub;
        private Integer lotteryTimes;
        private Integer totalTimes;
        private Integer browserTimes;
        private String link;
        private Boolean status;
        private Boolean gotStatus;
    }

}
