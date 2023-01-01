package cc.ussu.auth.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginParamVO implements Serializable {

    private static final long serialVersionUID = -9144988056057931681L;

    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String code;
    private String uuid;

}
