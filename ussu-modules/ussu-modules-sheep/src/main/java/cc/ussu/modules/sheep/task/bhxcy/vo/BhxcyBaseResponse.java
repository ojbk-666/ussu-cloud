package cc.ussu.modules.sheep.task.bhxcy.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BhxcyBaseResponse implements Serializable {

    private static final long serialVersionUID = -7683858131285340646L;

    /**
     * 账号被封禁
     */
    public static final Integer BAN = -100;

    /**
     * 接口响应码
     */
    private Integer result;
    /**
     * 响应消息
     */
    private String msg;

    public boolean isOk() {
        return 0 != result;
    }

}
