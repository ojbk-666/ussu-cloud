package cc.ussu.auth;

import cc.ussu.common.security.annotation.EnableCustomConfig;
import cc.ussu.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证服务
 */
@EnableCustomSwagger2
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableFeignClients(basePackages = "cc.ussu")
@EnableCustomConfig
public class UssuAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuAuthApplication.class, args);
    }

}
