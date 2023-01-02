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
import java.util.List;

/**
 * <p>
 * 题目
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DcPaperQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程id
     */
    private String courseId;

    /**
     * 试卷id
     */
    private String paperId;

    /**
     * 题型id 没有用
     */
    @TableField(exist = false)
    private String topicId;

    /**
     * 题目id
     */
    @TableId(type = IdType.INPUT)
    private String questionId;

    /**
     * 题目内容
     */
    private String questionTitle;

    /**
     * 题型代码
     */
    private String fullTopicTypeCd;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    // @JsonSerialize(using = LocalDateTimeSerializer.class)
    // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Long interfaceLogId;

    // 科目
    @TableField(exist = false)
    private DcCourse course;

    // 题型
    @TableField(exist = false)
    private DcPaperQuestionTopic topic;

    // 选项
    @TableField(exist = false)
    private List<DcQuestionOption> options;

}
