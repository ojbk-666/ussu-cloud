package cc.ussu.modules.sheep.task.jd.vo.joypark.response;

import cc.ussu.modules.sheep.task.jd.vo.joypark.JdJoyBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class GameShopListResponse extends JdJoyBaseResponse {

    private List<GameShop> data;

    @Data
    public static class GameShop {
        /**
         * 用户等级
         */
        private Integer userLevel;
        /**
         * 汪汪的名称
         */
        private String name;
        private Integer lightUp;
        /**
         * 消耗多少金币
         */
        private Integer consume;
        /**
         * 是否能购买 1能 0不能
         */
        private Integer shopStatus;
        /**
         * 购买需要的等级
         */
        private Integer buyLevel;
    }

}
