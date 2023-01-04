package cc.ussu.support.qinglong.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 青龙参数配置
 */
@Data
@Builder
@Accessors(chain = true)
public class QingLongConfig implements Serializable {

    private static final long serialVersionUID = -490926383133029018L;

    /**
     * 青龙所在基础url
     */
    private String url;

    /**
     * appid
     */
    private String clientId;

    /**
     * 密钥
     */
    private String clientSecret;

}
