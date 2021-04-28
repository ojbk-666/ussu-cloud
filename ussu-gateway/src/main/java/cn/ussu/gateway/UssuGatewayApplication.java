package cn.ussu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 网关
 */
// @SpringBootApplication(scanBasePackages = {"cn.ussu"})
@SpringBootApplication
public class UssuGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuGatewayApplication.class, args);
    }

}
