package cn.ussu.modules.dczx;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@EnableFeignClients
@MapperScan({"cn.ussu.modules.dczx.**.mapper"})
public class UssuDczxApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuDczxApplication.class, args);
    }

}
