package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice;

import lombok.Data;

@Data
public class StartItemResponse extends BaseClientHandleServiceExecuteResponse {

    private StartItemData data;

    @Data
    public static class StartItemData {
        private String uuid;
        private TaskInfo taskInfo;
    }

    @Data
    public static class TaskInfo {
        private Integer isTaskLimit;
        private Integer nextTreasureBoxNeed;
        private Integer nextTreasureBoxProgress;
        /**
         * 任务最大完成次数
         */
        private Integer taskCompletionLimit;
        /**
         * 已经完成的任务数
         */
        private Integer taskCompletionProgress;
        /**
         * 视频需要观看多长时间
         */
        private Integer videoBrowsing;
    }

}
