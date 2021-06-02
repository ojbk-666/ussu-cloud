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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
@ApiModel(value = "DcQuestionOption对象", description = "题目的选项")
// @Document(indexName = DcPaperQuestionSearch.INDEX_NAME, type = DcPaperQuestionSearch.QUESTION_OPTION_TYPE_NAME)
public class DcQuestionOption extends Model<DcQuestionOption> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long paperIdLong;

    private String paperIdStr;

    @ApiModelProperty(value = "题目表主键")
    private Long questionIdLong;

    @ApiModelProperty(value = "题目表question_id")
    private String questionIdStr;

    @ApiModelProperty(value = "OPTION_ID")
    private String optionId;

    // @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_smart")
    @ApiModelProperty(value = "OPTION_CONTENT")
    private String optionContent;

    @ApiModelProperty(value = "OPTION_TYPE")
    private String optionType;

    @ApiModelProperty(value = "是否是正确选项0否1是")
    private Boolean istrue;

    @ApiModelProperty(value = "selected")
    private String selected;

    // @Field(index = false, ignoreFields = "createTime")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // @Field(index = false, ignoreFields = "createBy")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    // @Field(index = false, ignoreFields = "interfaceLogId")
    @ApiModelProperty(value = "接口id")
    private Long interfaceLogId;

    // @Field(index = false, ignoreFields = "delFlag")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean delFlag;

}
