package cc.ussu.modules.sheep.task.jd.vo.factory.response;

import lombok.Data;

public class CollectScoreResponse extends JdFactoryBaseResponse<CollectScoreResponse.CollectScoreResult> {

    @Data
    public static class CollectScoreResult {
        private int acquiredScore;
        /**
         * 最大完成次数
         */
        private int maxTimes;
        /**
         * 获得多少电量
         */
        private String score;
        private int taskId;
        private int taskType;
        /**
         * 已完成次数
         */
        private int times;
        /**
         * 已有电池电量
         */
        private int userScore;
        private int taskStatus;
    }

}
