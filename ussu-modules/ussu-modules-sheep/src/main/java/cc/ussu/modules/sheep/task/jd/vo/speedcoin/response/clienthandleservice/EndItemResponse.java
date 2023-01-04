package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice;

import lombok.Data;

@Data
public class EndItemResponse extends BaseClientHandleServiceExecuteResponse{

    private EndItemData data;

    @Data
    public static class EndItemData {
        private StartItemResponse.TaskInfo taskInfo;
    }

}
