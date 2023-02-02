package cc.ussu.modules.system;

import cc.ussu.common.security.annotation.EnableCustomConfig;
import cn.easyes.starter.register.EsMapperScan;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 系统模块基础支撑
 */
@EnableFeignClients(basePackages = "cc.ussu")
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@MapperScan({"cc.ussu.modules.system.mapper"})
@EsMapperScan("cc.ussu.modules.system.es.mapper")
@EnableCustomConfig
public class UssuSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuSystemApplication.class, args);
    }

}
