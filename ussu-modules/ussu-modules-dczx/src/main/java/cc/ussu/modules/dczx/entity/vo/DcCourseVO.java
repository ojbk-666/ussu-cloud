package cc.ussu.modules.dczx.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DcCourseVO implements Serializable {

    private static final long serialVersionUID = 7267948945780031571L;
    private String courseName;
    private String courseKindName;
    private String examMethodGroupName;
    private String courseAttrName;
    private String studyCredit;
    private Integer courseFee;
    private String courseId;
    private String serviceCourseVersId;

}
