package cc.ussu.modules.sheep.task.jd.vo.joypark.response;

import cc.ussu.modules.sheep.task.jd.vo.joypark.JdJoyBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class TaskResponse extends JdJoyBaseResponse {

    private List<Task> data;

    @Data
    public static class Task {
        private Integer id;
        /**
         * 任务名称
         */
        private String taskTitle;
        /**
         * 任务类型
         * SIGN 每日签到得汪币
         * BROWSE_PRODUCT 逛商品得汪币
         * SHARE_INVITE 邀请得大量汪币
         * ORDER_MARK 下单立赢大量汪币
         * BROWSE_CHANNEL 带着汪汪去赛跑！
         */
        private String taskType;
        /**
         * 任务每天允许完成的次数
         */
        private Integer taskLimitTimes;
        private String taskShowTitle;
        private String taskImagUrl;
        private String shareMainTitle;
        private String shareSubTitle;
        private List<ConfigBase> configBaseList;
        /**
         * 已完成几次
         */
        public int taskDoTimes;
        private int taskShowRank;
        private int taskShowSwitch;
        private String taskSourceUrl;
        /**
         * 任务是否已经完成
         */
        private Boolean taskFinished;
        private String extendInfo1;
        private String canDrawAwardNum;
        private int timeControlSwitch;
        private String timePeriod;
        private String forwardUrl;

        public boolean isSignTask() {
            return "SIGN".equals(taskType);
        }

        public boolean isBrowseProductTask() {
            return "BROWSE_PRODUCT".equals(taskType);
        }

        public boolean isShareInviteTask() {
            return "SHARE_INVITE".equals(taskType);
        }

        public boolean isOrderMarkTask() {
            return "ORDER_MARK".equals(taskType);
        }

        public boolean isBrowseChannelTask() {
            return "BROWSE_CHANNEL".equals(taskType);
        }

        @Data
        public static class ConfigBase {
            private String awardName;
            private String awardTitle;
            private String awardIconUrl;
            private String awardGivenNumber;
            private Integer grantStandard;
        }
    }

}
