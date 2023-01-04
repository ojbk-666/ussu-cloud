package cc.ussu.modules.sheep.task.jd.vo.joypark.response;

import cc.ussu.modules.sheep.task.jd.vo.joypark.JdJoyBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ApTaskDetailResponse extends JdJoyBaseResponse {

    private ApTaskDetailData data;

    @Data
    public static class ApTaskDetailData {
        private Status status;
        private List<TaskItem> taskItemList;

        @Data
        public static class Status {
            /**
             * 是否完成
             */
            private Boolean finished;
            private String alreadyGranted;
            /**
             * 用户已完成次数
             */
            private Integer userFinishedTimes;
            /**
             * 完成需要的数量
             */
            private Integer finishNeed;
            private String awardInfo;
            private String canDrawAwardNum;
            private String activityCode;
            private String activityMsg;
        }
        @Data
        public static class TaskItem {
            // 可能没有
            private String appid;
            private String itemId;
            private String itemName;
            private String itemPic;
            private String itemType;
            private String itemParam;
            private Boolean taskInsert;
            private String clickUrl;
            private String exposalUrl;
            private String resourceType;
            private String jumpMode;
        }
    }

}
