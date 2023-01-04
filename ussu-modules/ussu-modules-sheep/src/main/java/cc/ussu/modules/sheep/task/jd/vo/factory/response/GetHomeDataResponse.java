package cc.ussu.modules.sheep.task.jd.vo.factory.response;

import lombok.Data;

@Data
public class GetHomeDataResponse extends JdFactoryBaseResponse<GetHomeDataResponse.GetHomeDataResult> {

    @Data
    public static class GetHomeDataResult {
        private int singleChargeLimitScore;

        /**
         * 为1表示是新用户
         */
        private int newUser;

        private AntiAddictionInfoVO antiAddictionInfoVO;

        private int haveProduct;

        /**
         * 用户名
         */
        private String userName;

        /**
         * 用户头像
         */
        private String userImg;

        private int upperChargeTimes;

        // private List<String> barrageList;

        private FactoryInfo factoryInfo;
    }

    @Data
    public static class AntiAddictionInfoVO {
        private String status;
    }

    /**
     * 工厂信息
     */
    @Data
    public static class FactoryInfo {

        private int couponCount;
        private String img;
        /**
         * 当前生产的电量
         */
        private String produceScore;
        /**
         * 是否满了
         */
        private boolean fullScore;
        /**
         * 商品总共需要的电量
         */
        private String totalScore;
        /**
         * 当前电池电量
         */
        private String remainScoreTxt;
        /**
         * 已经投入多少电量
         */
        private String useScore;
        /**
         * 电池容量
         */
        private Integer batteryCapacity;
        /**
         * 商品名称
         */
        private String name;
        /**
         * 当前电池电量
         */
        private String remainScore;
    }

}
