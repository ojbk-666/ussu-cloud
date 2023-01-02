package cc.ussu.modules.dczx.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AllStudyPlanVO implements Serializable {

    /**
     * 开始时间 yyyy-MM
     */
    private String beginTime;

    private List<StudyPlanVO> courseList;

    /**
     * 结束时间 yyyy-MM
     */
    private String endTime;

    /**
     * 1为当前学习计划 0否
     */
    private String isCurrent;

    /**
     * 第二阶段
     */
    private String termName;

    /**
     * 2
     */
    private Integer termSeq;

}
