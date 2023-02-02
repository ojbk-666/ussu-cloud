package cc.ussu.modules.system.es.domain;

import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;
import cn.easyes.annotation.rely.Analyzer;
import cn.easyes.annotation.rely.FieldType;
import cn.easyes.annotation.rely.IdType;
import cn.hutool.core.date.DatePattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@IndexName("ussu-cloud-system-log")
public class SystemLog implements Serializable {

    private static final long serialVersionUID = -3969552074966927177L;

    @IndexId(type = IdType.NONE)
    private String id;

    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.KEYWORD, searchAnalyzer = Analyzer.KEYWORD)
    private String serviceName;

    private String traceId;
    /**
     * 分组名称
     */
    private String group;
    /**
     * 名称
     */
    private String name;
    /**
     * 用户id
     */
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.KEYWORD, searchAnalyzer = Analyzer.KEYWORD)
    private String userId;
    /**
     * 用户账号
     */
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.KEYWORD, searchAnalyzer = Analyzer.KEYWORD)
    private String account;

    /**
     * 时间
     */
    @IndexField(fieldType = FieldType.DATE, dateFormat = DatePattern.NORM_DATETIME_PATTERN)
    private String createTime;
    /**
     * 请求参数
     */
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String params;
    /**
     * 响应结果
     */
    private String result;
    /**
     * 请求IP
     */
    @IndexField(fieldType = FieldType.IP)
    private String ip;
    /**
     * 请求发起的页面地址
     */
    private String referer;

}
