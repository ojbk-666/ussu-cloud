package cc.ussu.modules.sheep.task.jdbeans.vo;

import lombok.Data;

/**
 * 过期京豆兑换为喜豆响应
 */
@Data
public class ExpireJdExchangeJxBeansResponseVo {

    private String errId;
    private String errMsg;
    private String encryptCode;
    private String idc;
    private String nextUrl;
    // private Map order;

}
