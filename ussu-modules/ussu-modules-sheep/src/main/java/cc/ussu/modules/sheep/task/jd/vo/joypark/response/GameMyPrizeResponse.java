package cc.ussu.modules.sheep.task.jd.vo.joypark.response;

import cc.ussu.modules.sheep.task.jd.vo.joypark.JdJoyBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class GameMyPrizeResponse extends JdJoyBaseResponse {

    private GameMyPrizeData data;

    @Data
    public static class GameMyPrizeData {
        private GameBigPrizeVO gameBigPrizeVO;
        private List<GamePrizeItemVo> gamePrizeItemVos;

        @Data
        public static class GameBigPrizeVO {
            /**
             * 6.66元微信提现
             */
            private String bigPrizeName;
            private String bigPrizePublicityImg;
            /**
             * 所需等级
             */
            private Integer level;
            private Integer topLevelStatus;
            private String prizeType;
            private String prizeTypeVO;
        }

        @Data
        public static class GamePrizeItemVo {
            /**
             * 奖励等级
             */
            private Integer prizeLevel;
            /**
             * 奖励类型
             */
            private Integer prizeType;
            /**
             * 奖励名称
             */
            private String prizeName;

            private Integer status;

            /**
             * 领取过才有
             */
            private PrizeTypeVO prizeTypeVO;

            @Data
            public static class PrizeTypeVO {
                private Integer prizeUsed;
                private String batchId;
                private String couponKind;
                private String toUrl;
                private String shopId;
                private String forwardUrl;
                private String prizeEndTime;
                private String prizeValue;
                private String skuId;
                private String bigPrizeRealImg;
                private String realPrizeName;
                private String id;
                private String poolBaseId;
                private String prizeGroupId;
                private String prizeBaseId;
                private String stateMsg;
            }
        }
    }

}
