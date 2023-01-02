package cc.ussu.modules.dczx.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 学习计划
 * </p>
 *
 * @author mp-generator
 * @since 2022-09-09 21:41:09
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("dc_user_study_plan")
public class DcUserStudyPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String CURRENT_FLAG_TRUE = "1";
    public static final String CURRENT_FLAG_FALSE = "0";

    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
     * bbs状态
     */
    @TableField("bbs_status")
    private String bbsStatus;

    @TableField("button_list")
    private String buttonList;

    /**
     * 课程id
     */
    @TableField("course_id")
    private String courseId;

    @TableField("course_attr_id")
    private String courseAttrId;

    /**
     * 课程类型 专业课/公共基础课
     */
    @TableField("course_attr_name")
    private String courseAttrName;

    /**
     * 课程费用
     */
    @TableField("course_fee")
    private Integer courseFee;

    @TableField("course_kind_id")
    private String courseKindId;

    /**
     * 课程类型 必修/选修
     */
    @TableField("course_kind_name")
    private String courseKindName;

    /**
     * 课程名称
     */
    @TableField("course_name")
    private String courseName;

    @TableField("course_status")
    private String courseStatus;

    @TableField("course_status_id")
    private String courseStatusId;

    @TableField("course_term")
    private Integer courseTerm;

    @TableField("course_type_id")
    private Integer courseTypeId;

    @TableField("courseware_down_url")
    private String coursewareDownUrl;

    @TableField("courseware_url")
    private String coursewareUrl;

    @TableField("cx_num")
    private Integer cxNum;

    @TableField("daoxue_flag")
    private String daoxueFlag;

    /**
     * 分数
     */
    @TableField("display_score")
    private String displayScore;

    /**
     * 考核形式
     */
    @TableField("exam_method_group_name")
    private String examMethodGroupName;

    @TableField("exam_num")
    private Integer examNum;

    @TableField("plan_status")
    private String planStatus;

    @TableField("score_have_type")
    private String scoreHaveType;

    @TableField("service_course_vers_id")
    private String serviceCourseVersId;

    @TableField("stop_flag")
    private String stopFlag;

    /**
     * 学分
     */
    @TableField("study_credit")
    private String studyCredit;

    /**
     * 学习进度
     */
    @TableField("study_progress")
    private String studyProgress;

    @TableField("subject_course_id")
    private String subjectCourseId;

    /**
     * 用户id
     */
    @TableField("userid")
    private String userid;

    /**
     * 是否当前学习计划
     */
    private String currentFlag;

    private String beginTime;

    private String endTime;

    private String termName;

    private String createBy;

    private Date updateTime;

    /**
     * 逻辑删除
     */
    @JsonIgnore
    @TableLogic
    @TableField(value = "del_flag", fill = FieldFill.INSERT)
    private Boolean delFlag;

}
