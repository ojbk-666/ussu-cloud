package cc.ussu.auth.model.vo;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

@Data
public class GiteeAccessTokenResultVO {

    private String access_token;
    private String token_type;
    private Long expires_in;
    private String refresh_token;
    private String scope;
    private Long created_at;

    private String error;
    private String error_description;

    public boolean isSuccess() {
        return StrUtil.isBlank(error) && StrUtil.isBlank(error_description);
    }

}
