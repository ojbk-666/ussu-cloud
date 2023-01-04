package cc.ussu.modules.sheep.task.jd.vo.speedcoin.invite.response;

import cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice.BaseClientHandleServiceExecuteResponse;
import lombok.Data;

@Data
public class HelpResponse extends BaseClientHandleServiceExecuteResponse {

    private HelpData data;

    @Data
    public static class HelpData {
        private Integer coinReward;
    }

}
