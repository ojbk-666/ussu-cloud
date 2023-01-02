package cc.ussu.modules.dczx.entity.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class DcTrusteeshipVO implements Serializable {

    private static final long serialVersionUID = -327960783691719995L;

    /**
     * 主键
     */
    private String id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 托管状态 0正常 1暂停
     */
    @NotBlank(message = "状态不能为空")
    private String disableFlag;

    /**
     * 开启视频任务
     */
    @NotNull(message = "videoEnable不能为空")
    private Boolean videoEnable;

    /**
     * 视频播放速度
     */
    @NotNull(message = "视频倍速不能为空")
    @Min(value = 1, message = "视频倍速为1-10")
    @Max(value = 10, message = "视频倍速为1-10")
    private Integer videoSpeed = 5;

    /**
     * 是否跳过看视频过程
     */
    @NotNull(message = "videoJump不能为空")
    private Boolean videoJump;

    /**
     * 开启单元作业任务
     */
    @NotNull(message = "homeworkEnable不能为空")
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

}
