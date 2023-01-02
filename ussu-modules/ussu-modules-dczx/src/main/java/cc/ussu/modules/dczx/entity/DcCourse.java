package cc.ussu.modules.dczx.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
public class DcCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程id
     */
    @TableId(type = IdType.INPUT)
    private String courseId;

    private String courseAttrId;

    private String courseAttrName;

    private Integer courseFee;

    /**
     * 课程名称
     */
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

    // @JsonSerialize(using = LocalDateTimeSerializer.class)
    // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Long interfaceLogId;

}
