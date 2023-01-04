package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice;

import lombok.Data;

@Data
public class CashResponse extends BaseClientHandleServiceExecuteResponse {

    private CashData data;

    @Data
    public static class CashData {
        private String cashBalance;
        private Boolean cashExchanged;
        private Integer exchangeAmount;
        private Integer goldBalance;
        private String pin;
        private String todayExchange;
    }

}
