package cc.ussu.system.api.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class SystemLogVO implements Serializable {

    private static final long serialVersionUID = -3969552074966927177L;

    private String serviceName;
    private String traceId;
    /**
     * 分组名称
     */
    private String group;
    /**
     * 名称
     */
    private String name;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 童虎账号
     */
    private String account;

    /**
     * 时间
     */
    private Date createTime;
    /**
     * 请求参数
     */
    private String params;
    /**
     * 响应结果
     */
    private String result;
    /**
     * 请求IP
     */
    private String ip;
    /**
     * 请求发起的页面地址
     */
    private String referer;

}
