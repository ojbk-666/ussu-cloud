package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

/**
 * 喜豆响应
 */
@Data
public class JxBeanQuerybeanamountResponseVo {

    private Integer errcode;
    private String msg;
    private JxBeanQuerybeanamountDataVo data;

    @Data
    public class JxBeanQuerybeanamountDataVo {
        private Integer jingbean;
        private Integer limit;
        // private Integer switch;
        private Integer xibean;
    }

}
