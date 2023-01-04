package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

/**
 * 东东农场
 */
@Data
public class JdFruitInitForFarmResponseVo {

    private Integer code;
    private String message;

    private Integer toFlowTimes;
    private Integer assistPageType;
    private Integer oldUserState;
    private Integer lowFreqStatus;
    private Integer treeState;
    private Integer toFlowEnergy;
    private FarmUserPro farmUserPro;
    private Integer retainPopupLimit;
    private Integer toBeginEnergy;
    private Boolean enableSign;
    private Boolean hadCompleteXgTask;
    private Integer toFruitTimes;

    @Data
    public class FarmUserPro {
        private Integer totalEnergy;
        private Integer treeState;
        /**
         * 当前总水滴
         */
        private Integer treeEnergy;
        /**
         * 水果需要得总水滴
         */
        private Integer treeTotalEnergy;
        private String shareCode;
        private Integer winTimes;
        /**
         * 用户昵称
         */
        private String nickName;
        /**
         * 用户头像
         */
        private String imageUrl;
        /**
         * 水果类型 mihoutao22
         */
        private String type;
        /**
         * 水果名称
         */
        private String simpleName;
        /**
         * 水果名称
         */
        private String name;
        /**
         * 水果图片
         */
        private String goodsImage;
        private Integer newOldState;
        private Integer oldMarkComplete;
        private Integer commonState;
        private Integer prizeLevel;
    }

}
