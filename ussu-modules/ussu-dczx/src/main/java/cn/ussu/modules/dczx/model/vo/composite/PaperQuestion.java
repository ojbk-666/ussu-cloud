package cn.ussu.modules.dczx.model.vo.composite;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 题目
 *
 * @author liming
 * @date 2021-06-03 20:31
 */
@Data
@Accessors(chain = true)
public class PaperQuestion implements Serializable {

    private static final long serialVersionUID = -6285868449760263276L;

    private String TOPIC_ID;
    private String TOPIC_TYPE_CD;
    private String Fulltopictypecd;
    private String QUESTION_TYPE_NM;
    private String TOPIC_TYPE_ID;
    // 题目 只有一个
    private List<TopicTrunk> TOPIC_TRUNK;


}
