package cc.ussu.modules.dczx.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 学生基本信息
 * </p>
 *
 * @author liming
 * @since 2022-03-07 16:06:24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("dc_user_info")
public class DcUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 登录用户名
     */
    @TableId(value = "user_id", type = IdType.INPUT)
    private String userId;

    @TableField("accept_status")
    private String acceptStatus;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField("card_name")
    private String cardName;

    @TableField("card_id")
    private String cardId;

    @TableField("certificate_no")
    private String certificateNo;

    /**
     * 通讯地址
     */
    @TableField("commadd")
    private String commadd;

    /**
     * 工作单位
     */
    @TableField("company_address")
    private String companyAddress;

    @TableField("email")
    private String email;

    @TableField("stu_pic")
    private String stuPic;

    /**
     * 学习中心
     */
    @TableField("fdz_name")
    private String fdzName;

    /**
     * 毕业日期
     */
    @TableField("gra_date")
    private String graDate;

    /**
     * 毕业院校
     */
    @TableField("graduate_school")
    private String graduateSchool;

    /**
     * 手机
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 毕业专业
     */
    @TableField("pre_major")
    private String preMajor;

    /**
     * 入学批次
     */
    @TableField("rx_level")
    private String rxLevel;

    @TableField("sch_level")
    private String schLevel;

    /**
     * 性别
     */
    @TableField("sex")
    private String sex;

    @TableField("stu_id")
    private String stuId;

    /**
     * 姓名
     */
    @TableField("student_name")
    private String studentName;

    /**
     * 层次
     */
    @TableField("study_kind")
    private String studyKind;

    @TableField("study_mode")
    private String studyMode;

    @TableField("stufile_status")
    private String stufileStatus;

    @TableField("stufile_status_name")
    private String stufileStatusName;

    /**
     * 专业
     */
    @TableField("subject_name")
    private String subjectName;

    @TableField("tp_name")
    private String tpName;

    /**
     * 邮政编码
     */
    @TableField("zip")
    private String zip;

    private String jsessionid;

    private String loginName;

    private String password;

    private String loginIp;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 接口id
     */
    @TableField("interface_log_id")
    private Long interfaceLogId;

    @TableField("del_flag")
    @TableLogic
    private Boolean delFlag;

}
