package cc.ussu.modules.sheep.task.jd.vo.fruit.response;

import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import lombok.Data;

@Data
public class WaterFriendForFarmResponse extends JdBaseResponse {

    private CardInfo cardInfo;

    @Data
    public static class CardInfo {

        public static final String BEAN_CARD = "beanCard";
        public static final String FAST_CARD = "fastCard";
        public static final String DOUBLE_CARD = "doubleCard";
        public static final String SIGN_CARD = "signCard";

        private String type;
        private String rule;

        public String getCardName() {
            if (BEAN_CARD.equals(type)) {
                return "水滴换豆卡";
            } else if (FAST_CARD.equals(type)) {
                return "快速浇水卡";
            } else if (DOUBLE_CARD.equals(type)) {
                return "水滴翻倍卡";
            } else if (SIGN_CARD.equals(type)) {
                return "加签卡";
            }
            return rule;
        }
    }

}
