package cc.ussu.modules.sheep.task.jd.vo.wskey;

import lombok.Data;

import java.io.Serializable;

@Data
public class WsKeyJdResponse implements Serializable {

    private static final long serialVersionUID = 6517442561737381680L;

    private String code;

    private String url;

    private String tokenKey;

    public boolean ifSuccess() {
        return "0".equals(code);
    }

}
