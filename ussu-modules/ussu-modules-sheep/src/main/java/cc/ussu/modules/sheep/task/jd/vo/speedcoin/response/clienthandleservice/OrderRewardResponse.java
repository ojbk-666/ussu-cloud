package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice;

import lombok.Data;

import java.util.List;

@Data
public class OrderRewardResponse extends BaseClientHandleServiceExecuteResponse {

    private OrderRewardData data;

    @Data
    public static class OrderRewardData {
        /**
         * 所有奖励加起来的金额
         */
        private String totalAmount;
        /**
         * 单笔订单满多少金额才累计
         */
        private String orderAmount;
        /**
         * 已经提交的订单数量
         */
        private Integer submittedOrderQty;
        private Integer rewardType;
        private List<Detail> details;
        private String skuGroupId;
        private String rewardAmount;
        /**
         * 已领取的奖励金额
         */
        private String receivedAmount;
        private Integer needOrderQty;
    }

    @Data
    public static class Detail {
        /**
         * 0不能领取 1 2等待确认收货 4已领取
         */
        private Integer status;
        /**
         * 需要的订单量
         */
        private Integer orderQty;
        /**
         * 0红包 1微信零钱
         */
        private Integer type;
        /**
         * 红包金额
         */
        private String value;
        /**
         * 还差几单
         */
        private Integer needOrderQty;
    }

}
