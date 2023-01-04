package cc.ussu.modules.sheep.task.jd.vo.xmf.response;

import lombok.Data;

import java.util.List;

@Data
public class DoInteractiveAssignmentResponse {

    private String code;
    private String msg;
    private String subCode;
    private RewardsInfo rewardsInfo;

    @Data
    public static class RewardsInfo {
        private Integer poolRewardZeroStock;
        private List<Reward> successRewards;
        private List<Reward> failRewards;
    }

    @Data
    public static class Reward {

    }

}
