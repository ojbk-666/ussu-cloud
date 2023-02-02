package cc.ussu.modules.files.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@RefreshScope
@Component
@ConfigurationProperties(prefix = "oss.aliyun")
public class OssAliyunProperties {

    private String endpoint;

    private String bucket;
    private String accessKeyId;
    private String accessKeySecret;

    private String customDomain;

}
