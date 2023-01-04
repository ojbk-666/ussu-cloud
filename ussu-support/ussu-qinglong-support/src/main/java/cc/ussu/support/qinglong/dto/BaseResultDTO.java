package cc.ussu.support.qinglong.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 青龙返回的基础bean
 *
 * @param <T>
 */
@Data
public class BaseResultDTO<T> implements Serializable {

    private static final long serialVersionUID = -5123837990369543084L;

    private Integer code;
    private String message;
    private T data;

    public boolean isSuccess() {
        return 200 == code;
    }

}
