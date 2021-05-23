package cn.ussu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关
 */
@EnableDiscoveryClient
// @SpringBootApplication(scanBasePackages = {"cn.ussu"})
@SpringBootApplication
public class UssuGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuGatewayApplication.class, args);
    }

}
