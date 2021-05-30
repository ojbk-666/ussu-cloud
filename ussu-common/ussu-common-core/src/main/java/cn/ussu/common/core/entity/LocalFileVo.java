package cn.ussu.common.core.entity;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrPool;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LocalFileVo implements Serializable {

    private static final long serialVersionUID = -8178277332643834863L;

    /**
     * 文件访问域名
     */
    private String domain;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 文件访问的 domain + path
     */
    private String url;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 文件大小显示名称
     */
    private String sizeStr;
    /**
     * 文件名
     */
    private String name;
    /**
     * 原始文件名
     */
    private String originalName;
    /**
     * 创建时间
     */
    private Long timestamp;

    /**
     * 类型 目录folder
     */
    private String type;

    public LocalFileVo setTypeFolder() {
        this.type = "folder";
        return this;
    }

    public LocalFileVo setTypeFile(String typeFile) {
        this.type = typeFile;
        return this;
    }

    public LocalFileVo setSize(Long size) {
        this.size = size;
        if (size != null) {
            this.sizeStr = FileUtil.readableFileSize(size);
        }
        return this;
    }

    public LocalFileVo setDomain(String domain) {
        this.domain = domain;
        this.url = domain + StrPool.SLASH + getPath();
        return this;
    }

    public LocalFileVo setPath(String path) {
        this.path = path;
        this.url = getDomain() + StrPool.SLASH + path;
        return this;
    }
}
