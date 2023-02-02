package cc.ussu.auth.model.third;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class GiteeLoginParamVO {

    @NotBlank(message = "授权码不能为空")
    private String code;

}
