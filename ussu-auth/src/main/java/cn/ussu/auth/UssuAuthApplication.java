package cn.ussu.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证服务
 */
@SpringBootApplication(scanBasePackages = "cn.ussu.auth")
// @SpringBootApplication
@EnableFeignClients
public class UssuAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuAuthApplication.class, args);
    }

}
