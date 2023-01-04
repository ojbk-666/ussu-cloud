package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

/**
 * 秒杀响应
 */
@Data
public class JdMsResponseVo {

    private Integer code;
    private JdMsResultVo result;

    @Data
    public class JdMsResultVo {
        private JdMsResultAssignmentVo assignment;
    }

    @Data
    public class JdMsResultAssignmentVo {
        private String encryptProjectId;
        private String encryptAssignmentId;
        private String transferSubTitle;
        private Boolean isNewUserMark;
        private Integer assignmentTransferRedRatio;
        private Integer assignmentLowGrade;
        /**
         * 秒杀币
         */
        private Integer assignmentPoints;
    }

}
