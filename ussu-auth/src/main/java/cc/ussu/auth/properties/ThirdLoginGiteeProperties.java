package cc.ussu.auth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@RefreshScope
@ConfigurationProperties(prefix = "login.third.gitee")
public class ThirdLoginGiteeProperties {

    private String clientId;
    private String clientSecret;
    private String redirectUriLogin;
    private String redirectUriBind;

}
