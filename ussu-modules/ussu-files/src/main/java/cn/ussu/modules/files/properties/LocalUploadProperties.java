package cn.ussu.modules.files.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "file")
public class LocalUploadProperties {

    // 域名
    private String domain;
    // 前缀
    private String prefix;
    // 本地文件存储根路径
    private String localFilePath;

}
