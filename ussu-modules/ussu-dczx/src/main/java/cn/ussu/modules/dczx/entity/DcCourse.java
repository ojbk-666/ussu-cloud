package cn.ussu.modules.dczx.entity;

import cn.ussu.common.core.constants.StrConstants;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author liming
 * @since 2020-07-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DcCourse对象", description = "")
public class DcCourse extends Model<DcCourse> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String courseId;

    private String courseAttrId;

    private String courseAttrName;

    private Integer courseFee;

    private String courseName;

    private String courseKindId;

    private String courseKindName;

    private String courseStatus;

    private String courseStatusId;

    private Integer courseTerm;

    private Integer courseTypeId;

    private String coursewareDownUrl;

    private String coursewareUrl;

    private Integer cxNum;

    private String daoxueFlag;

    private String examMethodGroupName;

    private Integer examNum;

    private String mianxiuId;

    private String planStatus;

    private String scoreHaveType;

    private String serviceCourseVersId;

    private String stopFlag;

    private String studyCredit;

    private String subjectCourseId;

    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private Long interfaceLogId;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean delFlag;

}
