package cc.ussu.modules.sheep.task.jd.vo.speedsignfree;

import lombok.Data;

import java.util.List;

@Data
public class SignFreeHomeResponse extends JdSpeedBaseResponse {

    private Data1 data;

    @Data
    public static class Data1 {
        private Boolean newUser;
        private Boolean backRecord;
        private Boolean risk;
        private Integer surplusCount;
        /**
         * 总免单金额
         */
        private String sumTotalFreeAmount;
        /**
         * 要签到的商品列表
         */
        private List<SignFreeOrderInfo> signFreeOrderInfoList;

        @Data
        public static class SignFreeOrderInfo {
            private Integer id;
            /**
             * 商品名称
             */
            private String productName;
            /**
             * 商品图片
             */
            private String productImg;
            /**
             * 需要签到的天数
             */
            private Integer needSignDays;
            /**
             * 已经签到的天数
             */
            private Integer hasSignDays;
            /**
             * 免单金额
             */
            private String freeAmount;
            /**
             * 接受金额
             */
            private String moneyReceiveMode;
            /**
             * 订单id
             */
            private Long orderId;
            private Long surplusTime;
            private Integer combination;
        }
    }

}
