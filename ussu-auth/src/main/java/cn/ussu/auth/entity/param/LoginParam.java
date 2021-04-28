package cn.ussu.auth.entity.param;

import lombok.Data;

@Data
public class LoginParam {

    private String username;
    private String password;
    private String code;
    private String uuid;

}
