package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice;

import cn.hutool.core.util.BooleanUtil;
import lombok.Data;

@Data
public class BaseClientHandleServiceExecuteResponse {

    private Integer code;

    private Boolean isSuccess;

    private String message;

    private String requestId;

    public boolean isSuccess() {
        return 0 == code && BooleanUtil.isTrue(isSuccess);
    }

}
