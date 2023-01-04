package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

/**
 * 萌宠响应
 */
@Data
public class JdPetInitPetTownResponseVo {

    private String code;
    private String resultCode;
    private String message;
    private JdPetInitPetTownResult result;

    @Data
    public class JdPetInitPetTownResult {
        /**
         * 互助码
         */
        private String shareCode;
        private Boolean hisHbFlag;
        private Boolean helpSwitchOn;
        private Integer userStatus;
        /**
         * 总能量 狗粮
         */
        private Integer totalEnergy;
        private String shareTo;
        private Integer petSportStatus;
        private Boolean isAdult;
        /**
         * 勋章数
         */
        private Integer medalNum;
        /**
         * 和宠物相处多少天
         */
        private Integer meetDays;
        /**
         * 勋章进度 95.2
         */
        private Float medalPercent;
        private Boolean charitableSwitchOn;
        /**
         * 宠物信息
         */
        private PetInfo petInfo;
        /**
         * 还需要多少能量 狗粮
         */
        private Integer needCollectEnergy;
        /**
         * 剩余能量 狗粮
         */
        private Integer foodAmount;
        /**
         * 互助码
         */
        private String inviteCode;
        /**
         * 规则玩法 链接
         */
        private String rulesUrl;
        /**
         * 萌宠状态
         */
        private Integer petStatus;
        /**
         * 兑换商品信息
         */
        private GoodsInfo goodsInfo;
    }

    /**
     * 宠物信息
     */
    @Data
    public class PetInfo {
        private String advertId;
        /**
         * 宠物昵称
         */
        private String nickName;
    }

    /**
     * 商品信息
     */
    @Data
    public class GoodsInfo {
        /**
         * 商品名称
         */
        private String goodsName;
        /**
         * 商品图片
         */
        private String goodsUrl;
        private String goodsId;
        /**
         * 需要勋章数量
         */
        private Integer exchangeMedalNum;
        private String activityId;
        private String activityIds;
    }

}
