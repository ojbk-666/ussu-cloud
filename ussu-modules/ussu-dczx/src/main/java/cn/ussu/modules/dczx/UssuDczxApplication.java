package cn.ussu.modules.dczx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UssuDczxApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuDczxApplication.class, args);
    }

}
