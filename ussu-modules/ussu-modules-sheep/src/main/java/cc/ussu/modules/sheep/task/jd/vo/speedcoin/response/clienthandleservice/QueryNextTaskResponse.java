package cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice;

import lombok.Data;

@Data
public class QueryNextTaskResponse extends BaseClientHandleServiceExecuteResponse{

    private QueryNextTaskData data;

    @Data
    public static class QueryNextTaskData {
        private String nextResource;
        private Integer status;
    }

}
