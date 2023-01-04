package cc.ussu.modules.sheep.task.jd.vo.fruit.response;

import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.List;

@Data
public class TaskInitForFarmResponse extends JdBaseResponse {

    /**
     * 是否所有任务都完成
     */
    private Boolean allTaskFinished;

    /**
     * 签到任务
     */
    private SignInit signInit;

    /**
     * 广告浏览任务
     */
    private GotBrowseTaskAdInit gotBrowseTaskAdInit;

    private List<String> taskOrder;

    /**
     * 定时领水滴
     */
    private GotThreeMealInit gotThreeMealInit;

    /**
     * 首次浇水
     */
    private FirstWaterInit firstWaterInit;

    /**
     * 每日浇水10次奖励10水滴
     */
    private TotalWaterTaskInit totalWaterTaskInit;

    /**
     * 水滴雨
     */
    private WaterRainInit waterRainInit;

    /**
     * 给好友浇水任务
     */
    private WaterFriendTaskInit waterFriendTaskInit;

    /**
     * 签到任务
     */
    @Data
    public static class SignInit {
        private String signEnergyEachAmount;
        private Boolean f;
        private Integer totalSigned;
        private Boolean todaySigned;
    }

    /**
     * 广告浏览任务
     */
    @Data
    public static class GotBrowseTaskAdInit {
        private Boolean f;
        private List<UserBrowseTaskAd> userBrowseTaskAds;
    }

    @Data
    public static class UserBrowseTaskAd {
        /**
         * 任务id
         */
        private String advertId;
        /**
         * 浏览广告任务主标题
         */
        private String mainTitle;
        /**
         * 浏览广告任务子标题
         */
        private String subTitle;
        private String wechatPic;
        private String link;
        /**
         * 图片url
         */
        private String picurl;
        private String wechatLink;
        private String wechatMain;
        private String wechatSub;
        private String type;
        /**
         * 奖励多少水滴
         */
        private Integer reward;
        /**
         * 任务每天可以做几次
         */
        private Integer limit;
        /**
         * 应该是要求的浏览时间
         */
        private Integer time;
        /**
         * 广告通道 all wechat app
         */
        private String channel;
        private Integer hadGotTimes;
        private Integer hadFinishedTimes;

        /**
         * 自行扩展的字段
         * 是否显示在京东app
         * 以下条件会显示
         * 1.channel=app type=pages 标题mainTitle
         * 2.channel=all type=pages 标题mainTitle
         * 3.channel=app type=h5 标题mainTitle
         * 4.channel=all type=h5 标题mainTitle
         */
        public boolean showInJdApp() {
            return ("pages".equals(type) && StrUtil.containsAny(channel, "app", "all"))
                    || ("h5".equals(type) && StrUtil.containsAny(channel, "app", "h5"));
        }

        /**
         * 自行扩展的字段
         * 是否显示在微信小程序
         * 以下条件会显示
         * 1.channel=all type=pages 标题wechatTitle
         * 2.channel=wechat type=h5 标题wechatTitle
         * 3.channel=all type=h5 标题wechatTitle
         */
        public boolean showInWechatMp() {
            return ("pages".equals(type) && StrUtil.containsAny(channel, "all"))
                    || ("h5".equals(type) && StrUtil.containsAny(channel, "wechat", "all"));
        }

        public String getShowTitle() {
            if (showInJdApp()) {
                return mainTitle;
            } else if (showInWechatMp()) {
                return wechatMain;
            }
            return this.mainTitle;
        }
    }

    @Data
    public static class GotThreeMealInit {
        /**
         * 每次可得水滴多少 3-8 g
         */
        private String threeMealAmount;
        private Integer pos;
        private Boolean hadGotShareAmount;
        private Boolean f;
        /**
         * 参与时间段
         * 0-9 11-14 17-21
         */
        private List<String> threeMealTimes;
        private Boolean hadGotAmount;
    }

    /**
     * 首次浇水任务
     */
    @Data
    public static class FirstWaterInit {
        /**
         * 是否完成
         */
        private Boolean firstWaterFinished;
        private Boolean f;
        /**
         * 总完成次数
         */
        private Integer totalWaterTimes;
        /**
         * 奖励水滴数量
         */
        private Integer firstWaterEnergy;
    }

    /**
     * 每日浇水10次任务
     */
    @Data
    public static class TotalWaterTaskInit {
        /**
         * 是否完成
         */
        private Boolean totalWaterTaskFinished;
        /**
         * 是否完成
         */
        private Boolean f;
        /**
         * 需要完成次数
         */
        private Integer totalWaterTaskLimit;
        /**
         * 每次需要的水滴数
         */
        private Integer totalWaterTaskEnergy;
        /**
         * 已完成次数
         */
        private Integer totalWaterTaskTimes;
    }

    /**
     * 水滴雨
     */
    @Data
    public static class WaterRainInit {
        private Integer lastTime;
        /**
         * 已完成的次数
         */
        private Integer winTimes;
        private Boolean f;
        /**
         * 水滴雨配置
         */
        private Config config;
    }

    /**
     * 水滴雨配置
     */
    @Data
    public static class Config {
        /**
         * 每天最大次数
         */
        private Integer maxLimit;
        private Float peraward;
        /**
         * 最大水滴数
         */
        private Integer awardmax;
        private Integer intervalTime;
        private Integer lotteryProb;
        private String imgArea;
        private Integer countDown;
        private Integer countOfBomb;
        /**
         * 最小水滴
         */
        private Integer minAwardWater;
        /**
         * 最大水滴
         */
        private Integer maxAwardWater;
        private String bottomImg;
        private Integer actId;
        private String btnText;
        private String channel;
    }

    /**
     * 给好友浇水任务
     */
    @Data
    public static class WaterFriendTaskInit {
        /**
         * 给几个好友浇水
         */
        private Integer waterFriendMax;
        /**
         * 奖励多少水滴
         */
        private Integer waterFriendSendWater;
        /**
         * 已给几个好友浇水
         */
        private Integer waterFriendCountKey;
        private Boolean f;
        /**
         * 给好友浇水是否获得水滴
         */
        private Boolean waterFriendGotAward;
    }

}
