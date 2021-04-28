package cn.ussu.gateway.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 放行白名单配置
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "ignore")
public class IgnoreWhiteProperties {

    private List<String> whites = new ArrayList<>();

}
