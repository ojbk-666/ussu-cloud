package cn.ussu.common.core.entity;

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
    private String url = domain + StrPool.SLASH + path;
    /**
     * 文件大小
     */
    private Long size;
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

}
