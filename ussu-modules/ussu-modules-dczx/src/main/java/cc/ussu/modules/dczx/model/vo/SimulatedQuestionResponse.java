package cc.ussu.modules.dczx.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 模拟试题响应
 */
@Data
@Accessors(chain = true)
public class SimulatedQuestionResponse implements Serializable {

    private static final long serialVersionUID = -7241938684520232396L;

    // b5daaa7b-693d-4258-a481-8671dd6cc60d
    private String PAPERID;

    // 题目列表
    private List<PaperQuestion> PAPER_QUESTIONS;

    private Boolean finish;

    private String everytopicscore;

}
