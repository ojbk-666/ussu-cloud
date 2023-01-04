package cc.ussu.support.qinglong.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * token认证结果
 */
@Data
@Accessors(chain = true)
public class AuthTokenResultDTO extends BaseResultDTO<AuthTokenResultDTO.AuthTokenDataDTO> {

    @Data
    @Accessors(chain = true)
    public class AuthTokenDataDTO {

        private String token;
        private String token_type;
        private Long expiration;

    }

}
