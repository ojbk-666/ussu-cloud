package cc.ussu.modules.sheep.task.bhxcy.constants;

import lombok.Data;

import java.io.Serializable;

@Data
public class GlobalData implements Serializable {

    private static final long serialVersionUID = 7514299796892854089L;

    /**
     * 基础请求url
     */
    private String url;

    private Integer Verification;

    /**
     * 盐值
     */
    private String miyan;

    /**
     * api接口地址
     */
    private String api;

}
