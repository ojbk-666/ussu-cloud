package cc.ussu.modules.dczx.model.vo.homework;

import lombok.Data;

import java.util.List;

@Data
public class HomeWork {

    // ec3e0a43-cd97-4032-856a-c74d00c57e82
    private String WORKER_ID;

    /**
     * 单元作业名称
     * 《社会保险》第三套作业（9-11单元）
     */
    private String WORKER_NAME;

    /**
     * 单元作业的分数
     */
    private float WORKER_SCORE;

    private List<ExamResultInfo> examResultInfos;

    /**
     * 已做次数
     */
    private String DO_COUNT;

}
