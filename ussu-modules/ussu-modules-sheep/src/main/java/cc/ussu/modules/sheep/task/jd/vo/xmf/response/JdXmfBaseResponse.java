package cc.ussu.modules.sheep.task.jd.vo.xmf.response;

import lombok.Data;

@Data
public class JdXmfBaseResponse {

    protected Integer code;

    protected String msg;

    public boolean isSuccess() {
        return 0 == code;
    }

}
