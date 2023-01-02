package cc.ussu.modules.dczx.model.vo.homework;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 保存单元作业进度的参数
 */
@Data
@Accessors(chain = true)
public class SavemcapiParam implements Serializable {

    private static final long serialVersionUID = 7152729453623398896L;

    public static final String CHECKED_ON = "on";

    private String examPaper_Id;
    private String topictype_id;
    private String topic_id;
    private String topictrunk_id;
    private String generalanswer;
    private String type;
    private String checked;
    private String exampapertypeValue;

}
