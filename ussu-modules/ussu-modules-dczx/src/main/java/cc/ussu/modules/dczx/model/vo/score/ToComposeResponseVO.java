package cc.ussu.modules.dczx.model.vo.score;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 成绩构成的响应
 */
@Data
public class ToComposeResponseVO implements Serializable {

    private static final long serialVersionUID = 775753399733270407L;

    private long studentCourseId;
    private long subjectCourseId;
    private String courseId;
    private String courseName;
    private String courseStatusId;
    private int examNum;
    private int cxNum;
    private int maxScore;
    private int scoreCondition;
    private String ruleId;
    private String scoreHaveType;
    private List<MethodVO> methodList;
    private List<ChildrenVO> childrenList;
    private String query;
    private String compose;

}
