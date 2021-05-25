package cn.ussu.auth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "login.third.alipay")
public class ThirdLoginAlipayProperties {

    private String alipayGateway = "https://openapi.alipay.com/gateway.do";
    private String appid;
    private String privateKey;
    private String alipayPublicKey;

}
