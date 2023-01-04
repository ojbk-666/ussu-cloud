package cc.ussu.modules.sheep.task.jd.vo.xmf.response;

import lombok.Data;

@Data
public class GetInteractionHomeInfoResponse extends JdXmfBaseResponse {

    private GetInteractionHomeInfoResult result;

    @Data
    public static class GetInteractionHomeInfoResult {

        private Integer interactionId;
        // private FragmentConfig fragmentConfig;
        private Integer loginStatus;
        private TaskConfig taskConfig;
        private String title;
        private GiftConfig giftConfig;
    }

    @Data
    public static class GiftConfig {
        private String projectPoolId;
        private String projectId;
    }

    @Data
    public static class FragmentConfig {
        private String projectPoolId;
        private String projectId;
    }

    @Data
    public static class TaskConfig {
        private Integer skuJXTaskGrayTag;
        private Integer deepTaskGrayTag;
        private Integer calenderTaskGrayTag;
        private String projectPoolId;
        private String projectId;
    }
}
