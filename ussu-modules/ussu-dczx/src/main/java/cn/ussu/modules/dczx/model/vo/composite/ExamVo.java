package cn.ussu.modules.dczx.model.vo.composite;

import lombok.Data;

import java.util.List;

@Data
public class ExamVo {

    private String WORKER_ID;
    private String WORKER_NAME;
    private Integer WORKER_SCORE;
    private String DO_COUNT;
    private String questionCount;
    private String questionTime;
    private String timeOutFlag;
    private List<ExamResultInfo> examResultInfos;

}
