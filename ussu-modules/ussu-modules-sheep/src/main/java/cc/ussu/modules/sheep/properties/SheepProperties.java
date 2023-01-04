package cc.ussu.modules.sheep.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "sheep")
public class SheepProperties {

    /**
     * 日志存放的基础路径
     */
    private String logBasePath;

}
