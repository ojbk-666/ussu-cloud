package cc.ussu.modules.sheep;

import cc.ussu.common.security.annotation.EnableCustomConfig;
import cc.ussu.common.swagger.annotation.EnableCustomSwagger2;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCustomSwagger2
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@EnableFeignClients(basePackages = "cc.ussu")
@MapperScan({"cc.ussu.modules.sheep.**.mapper"})
@EnableCustomConfig
public class UssuSheepApplication {
    public static void main(String[] args) {
        SpringApplication.run(UssuSheepApplication.class);
    }
}
