package cn.ussu.modules.dczx.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author liming
 * @since 2020-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DcPaperQuestionTopic对象", description = "")
public class DcPaperQuestionTopic extends Model<DcPaperQuestionTopic> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long paperIdLong;

    private String paperIdStr;

    @ApiModelProperty(value = "TOPIC_ID")
    private String topicId;

    @ApiModelProperty(value = "TOPIC_TYPE_CD")
    private String topicTypeCd;

    @ApiModelProperty(value = "Fulltopictypecd")
    private String fullTopicTypeCd;

    @ApiModelProperty(value = "QUESTION_TYPE_NM")
    private String questionTypeNm;

    @ApiModelProperty(value = "TOPIC_TYPE_ID")
    private String topicTypeId;

    @ApiModelProperty(value = "接口id")
    private Long interfaceLogId;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean delFlag;

}
