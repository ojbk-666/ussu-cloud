package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice;

import cc.ussu.modules.sheep.task.jd.vo.speedcoin.TaskInfo;
import lombok.Data;

import java.util.List;

@Data
public class TaskListResponse extends BaseClientHandleServiceExecuteResponse {

    private List<TaskData> data;

    @Data
    public static class TaskData {
        private Integer showStyle;

        private TaskInfo taskInfo;

        private String taskName;

        private Integer taskType;
    }

}
