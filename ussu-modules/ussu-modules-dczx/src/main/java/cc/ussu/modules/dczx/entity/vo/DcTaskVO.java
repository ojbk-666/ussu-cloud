package cc.ussu.modules.dczx.entity.vo;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DcTaskVO implements Serializable {

    private static final long serialVersionUID = 5348624322333888992L;

    private String id;

    /**
     * 课程id
     */
    private String courseId;

    private String courseName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 任务耗时
     */
    private Long duration;

    /**
     * 任务耗时的中文表示 xx分钟xx秒
     */
    private String durationStr;

    public void setDuration(Long duration) {
        this.duration = duration;
        if (duration != null) {
            this.durationStr = DateUtil.formatBetween(duration, BetweenFormatter.Level.SECOND);
        }
    }

    /**
     * 状态 0未开始 1处理中 2完成 3失败
     */
    private Integer status;

    private String taskType;

    /**
     * 进度
     */
    private Integer progress;

    /**
     * 用户名
     */
    private String dcUsername;

    private String trusteeshipId;

    private String createBy;

    private String createName;

    private Date createTime;

    private String reason;

}
