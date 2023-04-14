package cc.ussu.auth.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GiteeUserInfoVO {

    private Long id;
    /**
     * 用户名 ojbk_666
     */
    private String login;
    /**
     * 昵称 我不是李四
     */
    private String name;
    /**
     * 头像 avatar_url
     */
    @JsonProperty("avatar_url")
    private String avatar_url;
    /**
     * 邮箱
     */
    private String email;

}
