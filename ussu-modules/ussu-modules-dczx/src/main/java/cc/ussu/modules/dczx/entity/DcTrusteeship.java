package cc.ussu.modules.dczx.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 托管列表
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-09 20:32:44
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("dc_trusteeship")
public class DcTrusteeship implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 站内信
     */
    public static final Integer NOTICE_METHOD_NOTICE= 1;
    /**
     * 邮件通知
     */
    public static final Integer NOTICE_METHOD_EMAIL= 2;
    /**
     * 单个任务完成时通知
     */
    public static final Integer NOTICE_TYPE_ONE_TASK = 1;
    /**
     * 所有任务完成时通知
     */
    public static final Integer NOTICE_TYPE_ALL_TASK = 2;

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 托管状态 0正常 1暂停
     */
    @TableField("disable_flag")
    private String disableFlag;

    /**
     * 开启视频任务
     */
    @TableField("video_enable")
    private Boolean videoEnable;

    /**
     * 是否跳过看视频过程
     */
    @TableField("video_jump")
    private Boolean videoJump;

    /**
     * 视频倍速
     */
    private Integer videoSpeed = 5;

    /**
     * 开启单元作业任务
     */
    @TableField("homework_enable")
    private Boolean homeworkEnable;

    /**
     * 单元作业目标分数
     */
    private Integer homeworkTargetScore = 85;

    /**
     * 单元作业单项最大完成次数
     */
    private Integer homeworkMaxCount = 5;

    /**
     * 是否开启综合作业
     */
    private Boolean compHomeworkEnable;

    /**
     * 综合作业目标分数
     */
    private Integer compHomeworkTargetScore = 85;

    /**
     * 综合作业最大完成次数
     */
    private Integer compHomeworkMaxCount = 5;

    /**
     * 是否发送通知
     */
    private Boolean sendNoticeFlag;

    /**
     * 通知方式 1站内信 2邮件通知
     */
    private Integer sendNoticeMethod = 1;

    /**
     * 通知类型 1单个任务完成时通知 2所有任务完成时通知
     */
    private Integer sendNoticeType = 1;

    /**
     * 通知id，站内信存用户id，邮件存邮箱
     */
    private String sendNoticeId;

    /**
     * 逻辑删除
     */
    @JsonIgnore
    @TableField("del_flag")
    @TableLogic
    private Boolean delFlag;

}
