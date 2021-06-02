package cn.ussu.modules.dczx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan({"cn.ussu.modules.dczx.**.mapper"})
public class UssuDczxApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuDczxApplication.class, args);
    }

}
