package cc.ussu.modules.sheep.task.jd.vo.joypark.response;

import cc.ussu.modules.sheep.task.jd.vo.joypark.JdJoyBaseResponse;
import lombok.Data;

@Data
public class JoyMoveResponse extends JdJoyBaseResponse {

    private JoyMoveData data;

    @Data
    public static class JoyMoveData {

    }

}
