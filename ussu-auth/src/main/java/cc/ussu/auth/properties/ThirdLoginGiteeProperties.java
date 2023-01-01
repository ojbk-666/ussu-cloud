package cc.ussu.auth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "login.third.gitee")
public class ThirdLoginGiteeProperties {

    private String clientId;
    private String clientSecret;
    private String redirectUri;

}
