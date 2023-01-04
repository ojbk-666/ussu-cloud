package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response;

import cc.ussu.modules.sheep.task.jd.vo.speedsignfree.JdSpeedBaseResponse;
import lombok.Data;

@Data
public class ApDoTaskResponse extends JdSpeedBaseResponse {

    private ApDoTaskData data;

    @Data
    public static class ApDoTaskData {
        private Boolean finished;
    }

}
