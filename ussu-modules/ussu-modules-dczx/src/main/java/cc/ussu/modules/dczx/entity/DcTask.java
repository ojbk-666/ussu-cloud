package cc.ussu.modules.dczx.entity;

import cc.ussu.modules.dczx.constants.DczxConstants;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 任务
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-01 15:52:07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("dc_task")
public class DcTask implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_VIDEO = "1";
    public static final String TYPE_HOMEWORK = "2";
    public static final String TYPE_COMP_HOMEWORK = "3";

    public static final Integer STATUS_WAIT = DczxConstants.TASK_STATUS_NOT_START;
    public static final Integer STATUS_DOING = DczxConstants.TASK_STATUS_DOING;
    public static final Integer STATUS_FINISH = DczxConstants.TASK_STATUS_FINISHED;
    public static final Integer STATUS_FAILED = DczxConstants.TASK_STATUS_ERROR;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 课程id
     */
    @TableField("course_id")
    private String courseId;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 状态 0未开始 1处理中 2完成 3失败
     */
    @TableField("status")
    private Integer status;

    /**
     * 进度
     */
    @TableField("progress")
    private Integer progress;

    /**
     * 用户名
     */
    @TableField("dc_username")
    private String dcUsername;

    @TableField("trusteeship_id")
    private String trusteeshipId;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 失败原因
     */
    private String reason;

    private String remark;

    private String taskLog;

    /**
     * 逻辑删除
     */
    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean delFlag;

}
