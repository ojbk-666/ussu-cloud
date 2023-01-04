package cc.ussu.modules.sheep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2022-10-16 09:38:58
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sheep_task")
public class SheepTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 任务名称
     */
    @TableField("task_name")
    private String taskName;

    /**
     * 定时规则
     */
    @TableField("cron")
    private String cron;

    /**
     * 日志路径
     */
    @TableField("log_path")
    private String logPath;

    /**
     * 目标类
     */
    @TableField("target_class")
    private String targetClass;

    private Boolean disabled;

    private Boolean running;

    /**
     * 最后运行时常
     */
    @TableField("last_run_duration")
    private String lastRunDuration;

    /**
     * 最后运行时间
     */
    @TableField("last_run_time")
    private Date lastRunTime;

    /**
     * 下一次执行时间
     */
    @TableField(exist = false)
    private Date nextFireTime;

}
