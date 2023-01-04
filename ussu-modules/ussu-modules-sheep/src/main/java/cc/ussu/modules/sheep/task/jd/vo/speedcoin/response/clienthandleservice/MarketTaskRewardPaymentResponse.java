package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice;

import lombok.Data;

@Data
public class MarketTaskRewardPaymentResponse extends BaseClientHandleServiceExecuteResponse{

    private MarketTaskRewardPaymentData data;

    @Data
    public static class MarketTaskRewardPaymentData {
        private Integer reward;
        private TaskInfo taskInfo;
    }

    @Data
    public static class TaskInfo {
        private String mainTitle;
    }

}
