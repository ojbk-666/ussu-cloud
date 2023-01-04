package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 总京豆响应
 */
@Data
@Accessors(chain = true)
public class TotalBeanResponseVo {

    private String retcode;
    private String msg;
    private Long timestamp;
    private TotalBeanResponseDataVo data;

    public boolean isOk() {
        return "0".equals(retcode);
    }

    @Data
    @Accessors(chain = true)
    public class TotalBeanResponseDataVo {

        private  AssetInfo assetInfo;
        private UserInfo userInfo;

    }

    @Data
    @Accessors(chain = true)
    public class AssetInfo {
        /**
         * 京豆数量
         */
        private String beanNum;
        /**
         * 优惠券数量
         */
        private String couponNum;
        /**
         * 白条
         */
        // private Map baitiaoInfo;
        /**
         * 红包
         */
        private String redBalance;
        private String accountBalance;
    }

    @Data
    @Accessors(chain = true)
    public class UserInfo {
        private UserInfoBaseInfo baseInfo;
        private String isJTH;
        private String isKaiPu;
        /**
         * plus 1是 0否
         */
        private String isPlusVip;
        private String isQQFans;
        private String isWxFans;
        /**
         * 是否实名认证
         */
        private String isRealNameAuth;
        private String jvalue;
        private String orderFlag;
        private String tmpActWaitReceiveCount;
        private String xbKeepOpenStatus;
        private String xbKeepScore;
        private String xbScore;
    }

    @Data
    public class UserInfoBaseInfo {
        private String accountType;
        private String baseInfoStatus;
        /**
         * pin
         */
        private String curPin;
        /**
         * 头像
         */
        private String headImageUrl;
        private String levelName;
        /**
         * 昵称
         */
        private String nickname;
        private String userLevel;
    }

}
