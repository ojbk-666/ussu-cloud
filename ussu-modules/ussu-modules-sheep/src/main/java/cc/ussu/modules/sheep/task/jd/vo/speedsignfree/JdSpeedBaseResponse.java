package cc.ussu.modules.sheep.task.jd.vo.speedsignfree;

import lombok.Data;

@Data
public class JdSpeedBaseResponse {

    private Boolean success;

    private Integer code;

    private String errMsg;

    public boolean isSuccess() {
        return true == success && 0 == code;
    }

}
