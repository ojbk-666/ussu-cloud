package cc.ussu.modules.dczx.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 题目选项对象
 *
 * @author liming
 * @date 2021-06-03 20:33
 */
@Data
@Accessors(chain = true)
public class TopicTrunk implements Serializable {
    private static final long serialVersionUID = -7727386844671078639L;

    private String QUESTION_ID;
    private String QUESTION_TITLE;
    private String TOPICTRUNK_TYPE;
    // 是否正确 1时为正确 取选中的值入库
    private String RIGHT;
    // 题目选项
    private List<QuestionOption> QUESTION_OPTIONS;

}
