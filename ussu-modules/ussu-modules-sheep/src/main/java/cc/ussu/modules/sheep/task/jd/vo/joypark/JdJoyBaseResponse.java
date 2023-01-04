package cc.ussu.modules.sheep.task.jd.vo.joypark;

import cn.hutool.core.util.BooleanUtil;
import lombok.Data;

@Data
public class JdJoyBaseResponse {

    private Boolean success;
    private Integer code;
    private String errMsg;

    public boolean isSuccess() {
        return BooleanUtil.isTrue(success);
    }

}
