package cc.ussu.modules.sheep.task.jdbeans.vo;

import cn.hutool.core.util.BooleanUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MyWalletInfoResponse implements Serializable {

    private Boolean success;
    private Integer resultCode;
    private String resultMsg;
    private Integer channelEncrypt;
    private ResultData resultData;

    public boolean isOk() {
        return BooleanUtil.isTrue(success) && resultData != null && BooleanUtil.isTrue(resultData.getSuccess());
    }

    @Data
    public static class ResultData {
        private String code;
        private Boolean success;
        private String message;
        private MyWalletInfo data;
    }

    @Data
    public static class MyWalletInfo {
        private int blackCardFlag;
        private List<Floor> floors;
        private String needAuth;

        @Data
        public static class Floor {
            private boolean rpc;

            private String rpcFlag;

            /**
             * app_assets 余额信息
             */
            private String floorId;

            private List<Node> nodes;

            private boolean success;

            private int sortId;

            private boolean closed;

            private String on;

            @Data
            public static class Node {
                private boolean rpc;

                // private Data data;

                private String rpcFlag;

                // private Title title;

                private boolean success;

                private int sortId;

                private boolean closed;

                private String nodeId;

                private String on;

                // private JumpInfo jumpInfo;
            }
        }
    }

}
