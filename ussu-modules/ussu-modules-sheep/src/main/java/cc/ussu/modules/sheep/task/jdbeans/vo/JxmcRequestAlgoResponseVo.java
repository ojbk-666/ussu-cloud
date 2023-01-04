package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

/**
 * 京喜牧场
 */
@Data
public class JxmcRequestAlgoResponseVo {

    private Integer status;
    private String message;
    private JxmcRequestAlgoDataVo data;

    @Data
    public class JxmcRequestAlgoDataVo {
        private String version;
        private JxmcRequestAlgoDataResultVo result;
    }

    @Data
    public class JxmcRequestAlgoDataResultVo {
        private String tk;
        private String algo;
    }

}
