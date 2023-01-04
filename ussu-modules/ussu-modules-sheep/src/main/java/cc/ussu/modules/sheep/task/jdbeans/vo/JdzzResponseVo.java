package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

/**
 * 京东转转响应
 */
@Data
public class JdzzResponseVo {

    private String code;
    private String message;
    private JdzzDataResponseVo data;

    @Data
    public class JdzzDataResponseVo {
        private Integer userStatus;
        /**
         * 标题 我的收益
         */
        private String headTitle;
        /**
         * 总金币
         */
        private String totalNum;
        /**
         * 可提现约24.92元
         */
        private String cashExpected;
        private String totalBeanNum;
        /**
         * 兑换比例 10000:1
         */
        private String cashRatio;
        /**
         * 金币提现
         */
        private String headButtonText;
    }

}
