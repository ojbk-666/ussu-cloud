package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice;

import lombok.Data;

@Data
public class RewardItemResponse extends BaseClientHandleServiceExecuteResponse{

    private RewardItemData data;

    @Data
    public static class RewardItemData {
        private Integer reward;
        private StartItemResponse.TaskInfo taskInfo;
    }

}
