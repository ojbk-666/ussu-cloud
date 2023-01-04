package cc.ussu.modules.sheep.task.jd.vo.fruit.response;

import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ClockInInitForFarmResponse extends JdBaseResponse {

    private Boolean f;
    private Integer totalSigned;
    private Boolean gotClockInGift;
    private Integer myFollowThemeConfigTimes;
    private Boolean todaySigned;

    private List<Theme> themes;

    // private List<Feed> feeds;

    private ClockInConfig clockInConfig;

    private Boolean isSignRemind;

    private List<VenderCoupon> venderCoupons;

    @Data
    public static class Theme {
        private String advertId;
        private String image;
        private String id;
        private String adDesc;
        private String name;
        private String link;
        private Boolean hadGot;
        private Boolean hadFollow;
        private Boolean filterFlag;
    }

    /*@Data
    public static class Feed {
        private String groupId;
        private String groupName;
        private String curStageId;
        private List<StageInfo> stageInfoList;
        private Integer areaStockStrategy;
        private Integer styleId;
        private Integer poolStyle;

        @Data
        public static class StageInfo {
            private String stageId;
            private String groupId;
            private String stageName;
            private String stageStatus;
            private String remainTime;
            private List<ProductInfo> productInfoList;
            private String deliveryType;
            private String deliveryId;
            private String stageET;
            private String materialType;

            @Data
            public static class ProductInfo {

            }

        }

    }*/

    @Data
    public static class VenderCoupon {
        private String advertId;
        private String image;
        private String id;
        private String adDesc;
        private String name;
        private String link;
        private Boolean hadGot;
        private Boolean hadFollow;
        private Boolean filterFlag;
    }

    @Data
    public static class ClockInConfig {
        private String advertId;
        private Integer signAward;
        private String surpriseType;
        private Integer minSurpriseAward;
        private Integer maxSurpriseAward;
        private Long time;
        private Long remindTime;
        private Integer followAward;
        private Integer followLimit;
        private Long followTime;
        private Integer couponAward;
        private Integer couponsLimit;
    }

}
