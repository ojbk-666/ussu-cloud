package cc.ussu.modules.dczx.model.vo.composite;

import lombok.Data;

import java.util.List;

@Data
public class ExamVo {

    private String WORKER_ID;
    private String WORKER_NAME;
    /**
     * 目前最高分数
     */
    private Integer WORKER_SCORE;
    /**
     * 完成的次数
     */
    private String DO_COUNT;
    private String questionCount;
    private String questionTime;
    private String timeOutFlag;
    private List<ExamResultInfo> examResultInfos;

}
