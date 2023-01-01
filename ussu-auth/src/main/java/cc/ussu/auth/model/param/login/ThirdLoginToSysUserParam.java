package cc.ussu.auth.model.param.login;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ThirdLoginToSysUserParam {

    private String account;
    private String name;
    private String nickName;
    private String email;
    private String avatar;
    private Integer sex;

    /**
     * 来源
     */
    private Integer source;

}
