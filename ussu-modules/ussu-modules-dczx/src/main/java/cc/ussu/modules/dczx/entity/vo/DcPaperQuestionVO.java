package cc.ussu.modules.dczx.entity.vo;

import cn.easyes.annotation.*;
import cn.easyes.common.constants.Analyzer;
import cn.easyes.common.enums.FieldType;
import cn.easyes.common.enums.IdType;
import cn.hutool.core.date.DatePattern;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@IndexName("dczx-question")
public class DcPaperQuestionVO implements Serializable {

    private static final long serialVersionUID = -78734071875720959L;

    @IndexId(type = IdType.CUSTOMIZE)
    private String questionId;

    private String courseId;

    @HighLight
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String questionTitle;

    private String fullTopicTypeCd;

    private String courseName;

    private String topicName;

    private String topicTypeCd;

    @IndexField(fieldType = FieldType.DATE, dateFormat = DatePattern.NORM_DATETIME_PATTERN)
    private Date createTime;

    @IndexField(fieldType = FieldType.NESTED, nestedClass = DcQuestionOptionVO.class)
    private List<DcQuestionOptionVO> options;

    @Score
    @IndexField(exist = false)
    private Float score;
}
