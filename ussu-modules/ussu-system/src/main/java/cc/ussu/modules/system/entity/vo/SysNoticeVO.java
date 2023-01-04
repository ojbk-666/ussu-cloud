package cc.ussu.modules.system.entity.vo;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysNoticeVO implements Serializable {

    private static final long serialVersionUID = -7199814858071702845L;

    private String id;

    @NotBlank(message = "通知标题不能为空")
    private String noticeTitle;

    @NotBlank(message = "通知类型不能为空")
    @Pattern(regexp = "[12]{1}", message = "通知类型只能为1 2")
    private String noticeType;

    private String noticeContent;

    private Boolean readFlag;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date readTime;

    private List<String> userIds;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date startTime;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date endTime;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date[] startEndTime;

    private String remark;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date createTime;

}
