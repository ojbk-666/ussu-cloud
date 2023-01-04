package cc.ussu.modules.system.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 通知公告表
 * </p>
 *
 * @author liming
 * @since 2022-01-18 15:31:26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_notice")
public class SysNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通知
     */
    public static final String NOTICE_TYPE_NOTICE = "1";
    /**
     * 公告
     */
    public static final String NOTICE_TYPE_ANNOUNCEMENT = "2";

    /**
     * 主键ID
     */
    @TableId("id")
    private String id;

    /**
     * 通知标题
     */
    @TableField("notice_title")
    private String noticeTitle;

    /**
     * 通知类型（1通知）
     */
    @TableField("notice_type")
    private String noticeType;

    /**
     * 接收人id
     */
    private String userId;

    /**
     * 已读状态1已读 0未读
     */
    @TableField("read_flag")
    private Boolean readFlag;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date readTime;

    /**
     * 通知内容id
     */
    private Long noticeContentId;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date endTime;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date createTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    @JsonIgnore
    @TableLogic
    private String delFlag;

}
