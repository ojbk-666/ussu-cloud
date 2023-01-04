package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response;

import cc.ussu.modules.sheep.task.jd.vo.speedsignfree.JdSpeedBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class WheelsHomeResponse extends JdSpeedBaseResponse {

    private WheelsHomeData data;

    @Data
    public static class WheelsHomeData {
        private List<PrizeItem> prizeItemList;

        private Integer lotteryChances;

        private String encryptPin;

        private String shareTaskToastNum;

        private String taskToastNum;

        private Boolean payLotteryFlag;

        private String skuId;

        private String skuPrice;

        private String skuNewPrice;

        private String lotteryTotalChances;

        private String maxLotteryChances;

        private String inviter;

        private String inviterMobile;

        private Boolean dailyFirst;
    }



    @Data
    public static class PrizeItem {
        private String prizeName;
        private String prizeImageUrl;
        private String prizeCode;
    }

}
