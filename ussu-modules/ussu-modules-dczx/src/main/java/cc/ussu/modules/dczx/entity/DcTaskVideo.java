package cc.ussu.modules.dczx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 视频信息
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-02 14:01:34
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("dc_task_video")
public class DcTaskVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 视频的vid
     */
    @TableId(value = "vid", type = IdType.INPUT)
    private String vid;

    /**
     * versionCode
     */
    @TableField("version_code")
    private String versionCode;

    @TableField("chapter_id")
    private String chapterId;

    @TableField("sub_chapter_id")
    private String subChapterId;

    @TableField("service_id")
    private String serviceId;

    @TableField("service_type")
    private String serviceType;

    /**
     * 视频url
     */
    @TableField("video_url")
    private String videoUrl;

    /**
     * 缩略图
     */
    @TableField("image")
    private String image;

    /**
     * 视频时长
     */
    @TableField("duration")
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
