package cc.ussu.system.api.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件信息的抽象
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class FileVO implements Serializable {

    private static final long serialVersionUID = 5550420853624155223L;

    /**
     * 文件名称
     */
    private String name;
    /**
     * 文件大小
     */
    private String length;

    private long size;
    /**
     * 最后修改时间
     */
    private Date updateTime;
    /**
     * 是否为文件夹
     */
    private Boolean isDirectory;
    /**
     * 文件访问url
     */
    private String url;
    /**
     * 图片缩略图
     */
    private String thumbnail;

}
