package cc.ussu.modules.dczx.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DcTaskVideoQueryVO implements Serializable {

    private static final long serialVersionUID = -3926989671394057264L;

    private String versionCode;

    /**
     * 课程id
     */
    private String courseId;

    /**
     * 单元标题
     */
    private String chapterTitle;

    /**
     * 是否有时长
     */
    private Boolean durationFlag;

}
