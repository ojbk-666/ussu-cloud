package cc.ussu.modules.dczx.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@RefreshScope
@Component
@ConfigurationProperties(prefix = "dczx")
public class DczxProperties implements Serializable {

    /**
     * 题目图片的本地存储位置
     */
    private String questionImgLocal;

    /**
     * 题目图片的访问基础路径（项目图片服务域名）
     */
    private String questionImgDomain;

    /**
     * 题目图片文件的域名（东财域名）
     */
    private String questionImgDownloadDomain;

}
