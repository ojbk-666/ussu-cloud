package cc.ussu.modules.sheep.task.jd.vo.factory.response;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 获取任务列表
 */
public class GetTaskDetailResponse extends JdFactoryBaseResponse<GetTaskDetailResponse.GetTaskDetailResult> {

    @Data
    public static class GetTaskDetailResult {
        private List<TaskVo> taskVos;
        private String userScore;
    }

    @Data
    public static class TaskVo {
        private AssistTaskDetailVo assistTaskDetailVo;
        private String acquiredScore;
        private String fromStatus;
        private String advGroupId;
        private List<FollowShopVo> followShopVo;
        private List<BrandMemberVo> brandMemberVos;
        private String groupId;
        private String icon;
        // private Map<String, Object> itemIdMap;
        private String maxScore;
        /**
         * 最多能做几次任务
         */
        private int maxTimes;
        private List<ProductInfoVo> productInfoVos;
        private String score;
        private List<ScoreRuleVo> scoreRuleVos;
        private List<SignAward> signAwardList;
        private SimpleRecordInfoVo simpleRecordInfoVo;
        private List<ShoppingActivityVo> shoppingActivityVos;
        private int status;
        /**
         * 任务子标题
         */
        private String subTitleName;
        private Long taskBeginTime;
        private Long taskEndTime;
        private int taskId;
        /**
         * 任务名称
         */
        private String taskName;
        /**
         * 任务类型 14 邀请好友
         */
        private int taskType;
        private List<ThreeMealInfoVo> threeMealInfoVos;
        private int times;
        private int waitDuration;
    }

    @Data
    public static class AssistTaskDetailVo {
        private String itemId;
        private String taskToken;
    }

    @Data
    public static class FollowShopVo {
        private String advGroupId;

        private String advertId;

        private String biclk;

        private List<String> comments;

        private String copy1;

        private String copy2;

        private String copy3;

        private String itemId;

        private String mcInfo;

        private String shopId;

        private String shopImage;

        private String shopName;

        private int status;

        private String taskToken;

        private String url;
    }

    @Data
    public static class BrandMemberVo {
        private String advGroupId;

        private String advertId;

        private List<String> comments;

        private String copy2;

        private String copy3;

        private String icon;

        private String itemId;

        private String memberUrl;

        private int status;

        private String taskToken;

        private String title;

        private String vendorIds;
    }

    @Data
    public static class ProductInfoVo {
        private String biclk;

        private String itemId;

        private String jdPrice;

        private String mcInfo;

        private String plusPrice;

        private String skuId;

        private String skuImage;

        private String skuName;

        private int status;

        private String taskToken;

    }

    @Data
    public static class ScoreRuleVo {
        private String score;
        private int scoreRuleType;
    }

    @Data
    public static class SignAward {
        private int dayNum;
        private String title;
    }

    @Data
    public static class SimpleRecordInfoVo {
        private String itemId;
        private String taskToken;
        private List<String> signList;
    }

    @Data
    public static class ShoppingActivityVo {
        private String advGroupId;
        private String advId;
        private String biclk;
        private List<String> comments;
        private String copy1;
        private String copy2;
        private String copy3;
        private String icon;
        private String itemId;
        private String mcInfo;
        private int status;
        private String subtitle;
        private String taskToken;
        private String title;
        private String url;
    }

    @Data
    public static class ThreeMealInfoVo {
        private String beginTime;
        private String endTime;
        private String itemId;
        private String itemName;
        private int status;
        private String taskToken;

        public boolean isInTime() {
            if (StrUtil.isBlank(beginTime) || StrUtil.isBlank(endTime)) {
                return false;
            }
            Date now = new Date();
            String date = DateUtil.formatDate(now);
            return DateUtil.isIn(now, DateUtil.parseDateTime(date + " " + beginTime), DateUtil.parseDateTime(date + " " + endTime));
        }
    }

}
