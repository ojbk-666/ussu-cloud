package cc.ussu.modules.dczx.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 学习计划
 */
@Data
public class StudyPlanVO implements Serializable {

    private static final long serialVersionUID = 6296019876217541775L;

    private String bbsStatus;
    private ButtonList buttonList;
    private String courseAttrId;
    /**
     * 学位课
     */
    private String courseAttrName;
    /**
     * 课程费用
     */
    private int courseFee;
    /**
     * B1002A
     */
    private String courseId;
    private String courseKindId;
    private String courseKindName;
    /**
     * courseName
     */
    private String courseName;
    /**
     * 课程状态
     * 考试通过
     */
    private String courseStatus;
    private String courseStatusId;
    private int courseTerm;
    private int courseTypeId;
    private String coursewareDownUrl;
    private String coursewareUrl;
    private int cxNum;
    private String daoxueFlag;
    /**
     * 分数
     */
    private String displayScore;
    private String examMethodGroupName;
    private int examNum;
    private String mianxiuId;
    private String mtntkStatus;
    private String planStatus;
    private String scoreHaveType;
    private String serviceCourseVersId;
    private String stopFlag;
    /**
     * 学分
     */
    private String studyCredit;
    private String studyProgress;
    private String subjectCourseId;

    @Data
    public static class ButtonList {
        private String batchValue;
        private String compHomework;
        private String compHomeworkTitle;
        private String compose;
        private String composeTitle;
        private String courseValue;
        private String download;
        private String downloadTitle;
        private String examOnPC;
        private String examOnPCTitle;
        private String examOnPCUrl;
        private String experiment;
        private String experimentTitle;
        private String experimentUrl;
        private String gradationValue;
        private String guide;
        private String guideTitle;
        private String guideUrl;
        private String homework;
        private String homeworkTitle;
        private String practice;
        private String practiceTitle;
        private String quiz;
        private String quizTitle;
        private String studyName;
        private String studyStatus;
        private String studyTitle;
        private String studyUrl;
        private String subjectValue;
        private String teachOnLive;
        private String teachOnLiveTitle;
        private String thesis;
        private String thesisTitle;
    }

}
