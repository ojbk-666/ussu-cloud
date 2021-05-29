package cn.ussu.modules.system;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 系统模块基础支撑
 */
@EnableFeignClients
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@MapperScan({"cn.ussu.modules.system.**.mapper"})
public class UssuSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuSystemApplication.class, args);
    }

}
