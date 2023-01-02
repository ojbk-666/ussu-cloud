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
 * 题目的选项
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DcQuestionOption implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 试卷id
     */
    private String paperId;

    /**
     * 题目id
     */
    private String questionId;

    @TableId(type = IdType.INPUT)
    private String optionId;

    /**
     * 选项内容
     */
    private String optionContent;

    private String optionType;

    /**
     * 是否是正确选项
     */
    private Boolean istrue;

    // @JsonSerialize(using = LocalDateTimeSerializer.class)
    // @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    private Long interfaceLogId;

}
