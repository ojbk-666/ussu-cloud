package cc.ussu.modules.dczx.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 学习计划
 */
@Getter
@Setter
@Accessors(chain = true)
public class DcUserStudyPlanVO implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String CURRENT_FLAG_TRUE = "1";
    public static final String CURRENT_FLAG_FALSE = "0";

    /**
     * 主键
     */
    private String id;

    /**
     * bbs状态
     */
    private String bbsStatus;

    private String buttonList;

    /**
     * 课程id
     */
    private String courseId;

    private String courseAttrId;

    /**
     * 课程类型 专业课/公共基础课
     */
    private String courseAttrName;

    /**
     * 课程费用
     */
    private Integer courseFee;

    private String courseKindId;

    /**
     * 课程类型 必修/选修
     */
    private String courseKindName;

    /**
     * 课程名称
     */
    private String courseName;

    private String courseStatus;

    private String courseStatusId;

    private Integer courseTerm;

    private Integer courseTypeId;

    private String coursewareDownUrl;

    private String coursewareUrl;

    private Integer cxNum;

    private String daoxueFlag;

    private String displayScore;

    /**
     * 考核形式
     */
    private String examMethodGroupName;

    private Integer examNum;

    private String planStatus;

    private String scoreHaveType;

    private String serviceCourseVersId;

    private String stopFlag;

    /**
     * 学分
     */
    private String studyCredit;

    /**
     * 学习进度
     */
    private String studyProgress;

    private String subjectCourseId;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 是否当前学习计划
     */
    private String currentFlag;

    private String beginTime;

    private String endTime;

    private String termName;

}
