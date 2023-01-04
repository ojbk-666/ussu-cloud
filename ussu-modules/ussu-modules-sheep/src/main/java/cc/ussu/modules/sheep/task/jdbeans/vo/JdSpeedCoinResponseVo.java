package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

/**
 * 极速版金币
 */
@Data
public class JdSpeedCoinResponseVo {

    private Integer code;
    private String message;
    private String requestId;
    private JdSpeedCoinDataVo data;

    @Data
    public class JdSpeedCoinDataVo {
        /**
         * 现金余额
         */
        private String cashBalance;
        /**
         * 现金兑换
         */
        private Boolean cashExchanged;
        /**
         * 兑换金额
         */
        private Integer exchangeAmount;
        /**
         * 总金币
         */
        private Integer goldBalance;
        private String pin;
        /**
         * 今日兑换比例
         */
        private String todayExchange;
    }

}
