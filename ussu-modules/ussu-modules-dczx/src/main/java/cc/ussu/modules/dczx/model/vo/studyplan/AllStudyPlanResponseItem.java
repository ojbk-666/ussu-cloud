package cc.ussu.modules.dczx.model.vo.studyplan;

import cc.ussu.modules.dczx.entity.DcCourse;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 全部学习计划的接口响应封装
 */
@Data
// @Accessors(chain = true)
public class AllStudyPlanResponseItem implements Serializable {

    private static final long serialVersionUID = -2086692740684378871L;

    private String beginTime;
    private List<DcCourse> courseList;
    private String endTime;
    private String isCurrent;
    private String termName;
    private Integer termSeq;

}
