package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response;

import cc.ussu.modules.sheep.task.jd.vo.speedsignfree.JdSpeedBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ApTaskListResponse extends JdSpeedBaseResponse {

    private List<ApTask> data;

    @Data
    public static class ApTask {
        private int id;

        private String taskTitle;

        private String taskType;

        private Integer taskLimitTimes;

        private String taskShowTitle;

        private String taskImagUrl;

        private String shareMainTitle;

        private String shareSubTitle;

        private List<ConfigBase> configBaseList;

        private Integer taskDoTimes;

        private Integer taskShowRank;

        private Integer taskShowSwitch;

        private String taskSourceUrl;

        private Boolean taskFinished;

        private String extendInfo1;

        private String canDrawAwardNum;

        private String timeControlSwitch;

        private String timePeriod;

        private String forwardUrl;

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
