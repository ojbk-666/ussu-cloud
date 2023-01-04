package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

/**
 * 领现金响应
 */
@Data
public class JdCashResponseVo {

    private Integer code;
    private String msg;
    private String echo;
    private JdCashDataVo data;

    @Data
    public class JdCashDataVo {
        private Integer bizCode;
        private Boolean success;
        private JdCashDataResultVo result;
    }

    @Data
    public class JdCashDataResultVo {
        /**
         * 应该是邀请码
         */
        private String invitedCode;
        /**
         * 总现金
         */
        private String totalMoney;
        private String signedStatus;
        /**
         * 任务信息
         */
        // private List<Object> taskInfos;
    }

}
