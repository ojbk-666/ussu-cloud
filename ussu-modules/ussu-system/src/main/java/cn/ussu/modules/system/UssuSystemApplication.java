package cn.ussu.modules.system;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统模块基础支撑
 */
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@MapperScan({"cn.ussu.modules.system.**.mapper"})
public class UssuSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuSystemApplication.class, args);
    }

}
