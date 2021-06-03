package cn.ussu.auth.model.param.login.system;

import lombok.Data;

@Data
public class LoginParam {

    private String username;
    private String password;
    private String code;
    private String uuid;

}
