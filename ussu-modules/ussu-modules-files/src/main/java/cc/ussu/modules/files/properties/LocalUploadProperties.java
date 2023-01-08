package cc.ussu.modules.files.properties;

import cn.hutool.core.util.StrUtil;
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
@ConfigurationProperties(prefix = "files")
public class LocalUploadProperties {

    /**
     * 域名
     */
    private String domain;
    /**
     * 上传文件存储在本地的根路径
     */
    private String localFilePath;
    /**
     * 资源映射路径前缀，/abc
     */
    private String localFilePrefix;

    public void setLocalFilePrefix(String localFilePrefix) {
        String tmp = StrUtil.addPrefixIfNot(StrUtil.removeSuffix(localFilePrefix, StrUtil.SLASH), StrUtil.SLASH);
        this.localFilePrefix = tmp;
    }

    /**
     * 获取域名，以/结尾
     */
    public String getDomainWithSuffixSlash() {
        return StrUtil.addSuffixIfNot(domain, StrUtil.SLASH);
    }

    /**
     * 获取域名，不以/结尾
     */
    public String getDomainWithoutSuffixSlash() {
        return StrUtil.removeSuffix(domain, StrUtil.SLASH);
    }

    /**
     * 获取文件上传到本地的路径，以/结尾
     */
    public String getLocalFilePathWithSuffixSlash() {
        return StrUtil.addSuffixIfNot(localFilePath, StrUtil.SLASH);
    }

    /**
     * 获取文件上传到本地的路径，不以/结尾
     */
    public String getLocalFilePathWithoutSuffixSlash() {
        return StrUtil.removeSuffix(localFilePath, StrUtil.SLASH);
    }

}
