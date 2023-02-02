package cc.ussu.auth.model.third;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class DingTalkLoginParamVO implements Serializable {

    private static final long serialVersionUID = -541828348006984970L;

    @NotBlank(message = "授权码不能为空")
    private String authCode;

    private String state;

}
