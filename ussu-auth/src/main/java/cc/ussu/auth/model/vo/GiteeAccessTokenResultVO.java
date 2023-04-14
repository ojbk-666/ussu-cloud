package cc.ussu.auth.model.vo;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GiteeAccessTokenResultVO {

    @JsonProperty("access_token")
    private String access_token;
    @JsonProperty("token_type")
    private String token_type;
    @JsonProperty("expires_in")
    private Long expires_in;
    @JsonProperty("refresh_token")
    private String refresh_token;
    private String scope;
    @JsonProperty("created_at")
    private Long created_at;

    private String error;
    @JsonProperty("error_description")
    private String error_description;

    public boolean isSuccess() {
        return StrUtil.isBlank(error) && StrUtil.isBlank(error_description);
    }

}
