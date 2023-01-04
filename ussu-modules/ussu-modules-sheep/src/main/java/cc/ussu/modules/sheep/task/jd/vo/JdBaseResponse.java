package cc.ussu.modules.sheep.task.jd.vo;

import lombok.Data;

@Data
public class JdBaseResponse {

    protected String code;
    protected String message;

    public boolean isSuccess() {
        return "0".equals(code);
    }

}
