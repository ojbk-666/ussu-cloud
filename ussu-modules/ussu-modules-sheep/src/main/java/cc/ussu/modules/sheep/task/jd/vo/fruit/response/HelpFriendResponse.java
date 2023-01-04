package cc.ussu.modules.sheep.task.jd.vo.fruit.response;

import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class HelpFriendResponse extends JdBaseResponse {

    private String degradeConfig;

    private IdCertification idCertification;

    private TurntableInit turntableInit;

    private MengchongResouce mengchongResouce;

    private boolean clockInGotWater;

    private boolean isOpenOldRemind;

    // private GuidPopupTask guidPopupTask;

    private int toFruitEnergy;

    // private H5ShieldConfig h5ShieldConfig;

    private String statisticsTimes;

    private Integer sysTime;

    private Integer shareChannelType;

    private Integer toFlowTimes;

    private Integer assistPageType;

    private String minSupportAPPVersion;

    private Integer lowFreqStatus;

    private boolean funCollectionHasLimit;

    private Integer treeState;

    private boolean iconFirstPurchaseInit;

    private Integer toFlowEnergy;

    private InitForFarmResponse.FarmUserPro farmUserPro;

    private Integer retainPopupLimit;

    private Integer toBeginEnergy;

    private boolean enableSign;

    // private LoadFriend loadFriend;

    private boolean hadCompleteXgTask;

    private List<Integer> oldUserIntervalTimes;

    private Integer toFruitTimes;

    private List<String> oldUserSendWater;

    private HelpResult helpResult;

    @Data
    public static class IdCertification {
        private Integer status;
    }

    @Data
    public static class TurntableInit {
        private Integer timeState;
    }

    @Data
    public static class MengchongResouce {
        private String advertId;
        private String name;
        private String appImage;
        private String appLink;
        private String cxyImage;
        private String cxyLink;
        private String type;
        private Boolean openLink;
        private String channelType;
    }

    @Data
    public static class HelpResult {
        /**
         * 今天剩余助力次数
         */
        private Integer remainTimes;
        /**
         * 助力好友获得多少g水滴
         */
        private Integer salveHelpAddWater;
        private Integer addFriend;
        private String code;
        /**
         * 被助力的好友信息
         */
        private MasterUserInfo masterUserInfo;
    }

    /**
     * 被助力的好友信息
     */
    @Data
    public static class MasterUserInfo {
        private Integer totalEnergy;
        private Integer treeState;
        private Integer createTime;
        private Integer treeEnergy;
        private Integer treeTotalEnergy;
        private String shareCode;
        private Integer winTimes;
        private String nickName;
        private String imageUrl;
        private String type;
        private String simpleName;
        private Integer prizeLevel;
    }

}
