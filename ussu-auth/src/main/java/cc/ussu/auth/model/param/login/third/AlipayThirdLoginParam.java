package cc.ussu.auth.model.param.login.third;

import lombok.Data;

@Data
public class AlipayThirdLoginParam {

    private String app_id;
    private String auth_code;
    private String scope;
    private String source;
    private String state;

}
