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
 * 题目
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DcPaperQuertion对象", description = "题目")
// @Document(indexName = DcPaperQuestionSearch.INDEX_NAME, type = DcPaperQuestionSearch.QUESTION_TYPE_NAME)
public class DcPaperQuestion extends Model<DcPaperQuestion> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String courseIdStr;

    private Long paperIdLong;

    private String paperIdStr;

    private Long topicIdLong;

    private String topicIdStr;

    @ApiModelProperty(value = "QUESTION_ID")
    private String questionId;

    @ApiModelProperty(value = "QUESTION_TITLE")
    // @Field(type = FieldType.Text,analyzer = "ik_smart",searchAnalyzer = "ik_smart")
    private String questionTitle;

    @ApiModelProperty(value = "TOPICTRUNK_TYPE")
    private String topictrunkType;

    // @Field(index = false, ignoreFields = "createBy")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    // @Field(index = false, ignoreFields = "createTime")
    @JsonFormat(pattern = StrConstants.DEFAULT_TIME_PATTERN)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // @Field(index = false, ignoreFields = "interfaceLogId")
    @ApiModelProperty(value = "接口id")
    private Long interfaceLogId;

    // @Field(index = false, ignoreFields = "delFlag")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean delFlag;

    // 来源 10 接口采集 20 手动录入
    private Integer source;

    // 科目
    // @TableField(exist = false)
    // private DcCourse course;

    // 题型
    // @Field(index = false, ignoreFields = "topic")
    // @TableField(exist = false)
    // private DcPaperQuestionTopic topic;

    // 选项
    // @TableField(exist = false)
    // private List<DcQuestionOption> options;

}
