package cc.ussu.modules.sheep.task.jd.vo.fruit.response;

import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class MyCardInfoForFarmResponse extends JdBaseResponse {

    /**
     * 水滴翻倍卡
     */
    private Integer doubleCard;
    private String statisticsTimes;
    /**
     * 快速浇水卡
     */
    private Integer fastCard;
    private Long sysTime;
    /**
     * 卡片信息
     */
    private List<CardInfo> cardInfos;
    /**
     * 加签卡
     */
    private Integer signCard;
    /**
     * 水滴换豆卡
     */
    private Integer beanCard;

    /**
     * 卡片信息
     */
    @Data
    public static class CardInfo {

        public static final String BEAN_CARD = "beanCard";
        public static final String FAST_CARD = "fastCard";
        public static final String DOUBLE_CARD = "doubleCard";
        public static final String SIGN_CARD = "signCard";

        private String type;
        /**
         * 京豆数量
         */
        private Integer beanAward;
        /**
         * 每日最多使用几次
         */
        private Integer useTimesInDay;
        private Integer version;
        /**
         * 使用规则
         */
        private String rule;
        /**
         * 获取方式
         */
        private String way;
        /**
         * 子标题
         */
        private String cardSubTitle;
    }

}
