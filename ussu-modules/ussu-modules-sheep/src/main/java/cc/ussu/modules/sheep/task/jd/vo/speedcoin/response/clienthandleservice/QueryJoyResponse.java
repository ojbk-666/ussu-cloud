package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice;

import cc.ussu.modules.sheep.task.jd.vo.speedcoin.TaskInfo;
import lombok.Data;

import java.util.List;

@Data
public class QueryJoyResponse extends BaseClientHandleServiceExecuteResponse {

    private QueryJoyData data;

    @Data
    public static class QueryJoyData {
        private List<TaskBubble> taskBubbles;
        private BalanceVO balanceVO;
        private List<JoyResource> joyResourceList;
        private Boolean newOneMarkOnOff;
        private String notice;
        private SuperAdVo superAdVo;
    }

    @Data
    public static class TaskBubble {
        private String id;
        private String activeType;
    }

    @Data
    public static class BalanceVO {
        private String cashBalance;
        private String estimatedAmount;
        private String exchangeGold;
        private String formatGoldBalance;
        private Integer goldBalance;
    }
    @Data
    public static class JoyResource {
        private String image;
        private Integer isOpen;
        private String jumpLink;
    }
    @Data
    public static class SuperAdVo {
        private List<DiversionTask> diversionTaskList;
        private List<EveryDayTask> everyDayTaskList;
    }

    @Data
    public static class DiversionTask {
        private Integer showStyle;
        private TaskInfo taskInfo;
        private String taskName;
        private Integer taskType;
    }

    @Data
    public static class EveryDayTask {
        private Integer showStyle;
        private TaskInfo taskInfo;
        private String taskName;
        private Integer taskType;
    }

}
