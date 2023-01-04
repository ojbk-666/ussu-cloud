package cc.ussu.modules.sheep.task.jd.vo.xmf.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QueryInteractiveInfoResponse {

    private String code;
    private String msg;

    public boolean isSuccess() {
        return "0".equals(code);
    }

    private List<Assignment> assignmentList;

    @Data
    public static class Assignment {
        private String encryptAssignmentId;
        private String assignmentName;
        private String assignmentDesc;
        private String assignmentImg;
        private Date assignmentStartTime;
        private Date assignmentEndTime;
        private Integer assignmentType;
        private Integer assignmentTimesLimit;
        private UserVerificationInfo userVerificationInfo;
        private Boolean completionFlag;
        private Integer completionCnt;
        private List<Reward> rewards;
        private Ext ext;
        private Integer timeStatus;
        private Integer timesLimitType;
        private Integer exchangeRate;
        private Integer scoreExchangeId;
    }

    @Data
    public static class Ext {
        private Boolean dynamicTaskIcon;
        private Boolean dynamicLbs;
        private List<AddCart> addCart;
        private List<ShoppingActivity> shoppingActivity;
        private List<ProductsInfo> productsInfo;
        private List<BrowseShop> browseShop;
        private Sign1 sign1;
        private Integer outerTaskType;
        /**
         * shoppingActivity
         * addCart
         * productsInfo
         * browseShop
         * brandMemberList
         * sign1
         */
        private String extraType;
        private Boolean dynamicTaskName;
    }

    @Data
    public static class AddCart {
        private String pPrice;
        private String pcpPrice;
        private String venderId;
        private String plusPrice;
        private String biclk;
        private String skuName;
        private String itemId;
        private String mcInfo;
        private String jdPrice;
        private String skuId;
        private String skuImage;
        private Integer status;
    }

    @Data
    public static class ShoppingActivity {
        private List<String> comments;
        private String advId;
        private String icon;
        private String title;
        private String biclk;
        private String url;
        private String itemId;
        private String mcInfo;
        private String subtitle;
        private String copy3;
        private String tasktoken;
        private String copy2;
        private String copy1;
        private Integer status;
    }

    @Data
    public static class ProductsInfo {
        private String pPrice;
        private String pcpPrice;
        private String venderId;
        private String plusPrice;
        private String biclk;
        private String skuName;
        private String itemId;
        private String mcInfo;
        private String jdPrice;
        private String skuId;
        private String skuImage;
        private Integer status;
    }

    @Data
    public static class BrowseShop {
        private BrowseShopExt ext;
        private String itemId;
        private String mcInfo;
        private List<String> comments;
        private String shopImage;
        private String venderId;
        private String shopName;
        private String shopId;
        private String biclk;
        private Integer status;
    }

    @Data
    public static class BrowseShopExt {
        private String groupId;
    }

    @Data
    public static class Sign1 {
        private List<String> signList;
        private String itemId;
        private Integer signDays;
        private Integer status;
    }

    @Data
    public static class Reward {
        private String rewardImg;

        private String couponLimitStr;

        private String rewardValue;

        private Long activityBeginTime;

        private String rewardDesc;

        private Long inSendTime;

        private Long couponBeginTime;

        private Integer rewardType;

        private Integer couponKind;

        private String discountDesc;

        private String sendBeginTime;

        private Integer batchId;

        private Integer couponStyle;

        private Long couponEndTime;

        private String rewardName;

        private Integer couponDiscount;

        private Integer rewardId;

        private Integer condition;

        private Long activityEndTime;

        private Integer couponType;

        private Integer poolId;

        private Integer couponQuota;

        private String sendEndTime;
    }

    @Data
    public static class UserVerificationInfo {
        private Boolean userQulification;
        private Boolean userScore;
    }

}
