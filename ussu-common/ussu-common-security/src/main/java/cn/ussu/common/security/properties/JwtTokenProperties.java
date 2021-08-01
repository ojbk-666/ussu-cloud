package cn.ussu.common.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwttoken")
public class JwtTokenProperties {

    /**
     * jwt密钥
     */
    private String key = "lJDofA%ks0-SK8FE3798*79293F&*F@#fslihFekwew97efF";

    /**
     * token过期时间
     */
    private Integer timeout = 30;

}
