package cc.ussu.modules.dczx.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DcTaskVideoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 视频的vid
     */
    private String vid;

    /**
     * versionCode
     */
    private String versionCode;

    private String courseId;

    private String courseName;

    private String chapterId;

    private String subChapterId;

    private String serviceId;

    private String serviceType;

    /**
     * 视频url
     */
    private String videoUrl;

    /**
     * 缩略图
     */
    private String image;

    /**
     * 视频时长
     */
    private Integer duration;

    /**
     * 单元标题
     */
    private String chapterTitle;

    /**
     * 单元任务标题
     */
    private String divisionCourseTitle;

}
