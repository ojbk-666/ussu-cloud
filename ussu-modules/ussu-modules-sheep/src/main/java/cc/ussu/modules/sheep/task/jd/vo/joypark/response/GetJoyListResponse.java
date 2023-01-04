package cc.ussu.modules.sheep.task.jd.vo.joypark.response;

import cc.ussu.modules.sheep.task.jd.vo.joypark.JdJoyBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class GetJoyListResponse extends JdJoyBaseResponse {

    private GetJoyListData data;

    @Data
    public static class GetJoyListData {
        private List<ActivityJoy> activityJoyList;
        private List<WorkJoyInfo> workJoyInfoList;

        @Data
        public static class ActivityJoy {
            private Integer id;
            private String name;
            private Integer level;
        }
        @Data
        public static class WorkJoyInfo {
            private Integer location;
            private Boolean unlock;
            private JoyDTO joyDTO;

            @Data
            public static class JoyDTO {
                private Integer id;
                private String name;
                private Integer level;
            }
        }
    }

}
