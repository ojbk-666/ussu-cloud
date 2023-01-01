package cc.ussu.auth.model.vo;

import lombok.Data;

@Data
public class GiteeUserInfoVo {

    private Long id;
    private String login;
    private String name;
    private String avatar_url;
    private String email;

}
