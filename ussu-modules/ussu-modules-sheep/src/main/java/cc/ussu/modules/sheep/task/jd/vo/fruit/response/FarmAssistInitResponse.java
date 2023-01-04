package cc.ussu.modules.sheep.task.jd.vo.fruit.response;

import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class FarmAssistInitResponse extends JdBaseResponse {

    private Integer amount;
    private Boolean hasAssistFull;
    private Boolean f;
    private Long statisticsTimes;
    private String tipsToast;
    private List<AssistFriend> assistFriendList;
    private Integer status;
    private List<AssistStage> assistStageList;

    /**
     * 助力好友
     */
    @Data
    public static class AssistFriend {
        /**
         * 昵称
         */
        private String nickName;
        /**
         * 用户头像
         */
        private String image;
        /**
         * 助力时间
         */
        private Long time;
    }

    /**
     * 助力阶段
     */
    @Data
    public static class AssistStage {
        /**
         * 可领取的水滴数
         */
        private Integer waterEnergy;
        /**
         * 需要几个好友助力
         */
        private Integer assistNum;
        /**
         * 状态 3已领取 2未领取 1未满足条件
         */
        private Integer stageStaus;
        /**
         * 序号
         */
        private Integer stage;
        /**
         * 助力进度
         */
        private String percentage;
    }

}
