package cc.ussu.modules.sheep.task.jd.vo.fruit.response;

import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import lombok.Data;

@Data
public class InitForFarmResponse extends JdBaseResponse {

    /**
     * 是否在定时领水滴时间
     */
    private Boolean clockInGotWater;

    private Integer toFruitEnergy;

    private Long sysTime;

    private Integer toFlowTimes;

    private TodayGotWaterGoalTask todayGotWaterGoalTask;

    /**
     * 最小支持的App版本
     */
    private String minSupportAPPVersion;
    private Integer lowFreqStatus;

    private Integer treeState;

    private Integer toFlowEnergy;
    private FarmUserPro farmUserPro;
    private Integer retainPopupLimit;
    private Integer toBeginEnergy;
    private Boolean enableSign;
    private Integer toFruitTimes;

    /**
     * 已成熟
     */
    public boolean treeStateFinished() {
        return 2 == treeState || 3 == treeState;
    }

    /**
     * 种植中
     */
    public boolean treeStatePlanting() {
        return 1 == treeState;
    }

    /**
     * 未开始种植
     */
    public boolean treeStateNotPlant() {
        return 0 == treeState;
    }

    @Data
    public static class TodayGotWaterGoalTask {
        private Boolean canPop;
    }

    @Data
    public static class FarmUserPro {
        /**
         * 当前水滴
         */
        private Integer totalEnergy;
        /**
         * 果树状态 1种植中 23已成熟 0已下单购买, 但未开始种植新的水果
         */
        private Integer treeState;
        private Long createTime;
        /**
         * 果树目前水滴
         */
        private Integer treeEnergy;
        /**
         * 果树需要的总水滴
         */
        private Integer treeTotalEnergy;
        /**
         * 助力码
         */
        private String shareCode;
        /**
         * 已成功兑换水果的次数
         */
        private Integer winTimes;
        /**
         * 京东账号的昵称
         */
        private String nickName;
        /**
         * 头像
         */
        private String imageUrl;
        /**
         * 种植水果的类型
         */
        private String type;
        /**
         * 水果的简称
         */
        private String simpleName;
        /**
         * 水果的名称
         */
        private String name;
        /**
         * 水果图片
         */
        private String goodsImage;
        /**
         * SKU_ID
         */
        private String skuId;
        private Long lastLoginDate;
        private Integer newOldState;
        private Integer commonState;
        /**
         * 水果等级?
         */
        private Integer prizeLevel;
    }

}
