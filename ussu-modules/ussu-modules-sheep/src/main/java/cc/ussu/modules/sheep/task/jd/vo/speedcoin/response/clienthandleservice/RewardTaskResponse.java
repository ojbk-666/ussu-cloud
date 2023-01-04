package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice;

import lombok.Data;

@Data
public class RewardTaskResponse extends BaseClientHandleServiceExecuteResponse{

    private RewardTaskData data;

    @Data
    public static class RewardTaskData {
        private Integer reward;
    }

}
