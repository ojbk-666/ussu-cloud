package cc.ussu.modules.log.es.domain;

import cn.easyes.annotation.*;
import cn.easyes.annotation.rely.Analyzer;
import cn.easyes.annotation.rely.FieldType;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@IndexName("ussu-cloud-log")
public class LogEntity {

    @IndexId
    private String id;
    @IndexField(fieldType = FieldType.KEYWORD, analyzer = Analyzer.KEYWORD, searchAnalyzer = Analyzer.KEYWORD)
    private String threadName;
    @IndexField(fieldType = FieldType.KEYWORD, analyzer = Analyzer.KEYWORD, searchAnalyzer = Analyzer.KEYWORD)
    private String loggerName;
    @IndexField(fieldType = FieldType.KEYWORD, analyzer = Analyzer.KEYWORD, searchAnalyzer = Analyzer.KEYWORD)
    private String level;
    @HighLight
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String formattedMessage;
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String exception;
    @IndexField(fieldType = FieldType.LONG, analyzer = Analyzer.KEYWORD, searchAnalyzer = Analyzer.KEYWORD)
    private long timeStamp;

    @IndexField(fieldType = FieldType.KEYWORD, analyzer = Analyzer.KEYWORD, searchAnalyzer = Analyzer.KEYWORD)
    private String traceId;

    @Score
    @IndexField(exist = false)
    private Float score;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @IndexField(exist = false)
    private Date startTime;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @IndexField(exist = false)
    private Date endTime;

    public LogEntity() {
    }
}
