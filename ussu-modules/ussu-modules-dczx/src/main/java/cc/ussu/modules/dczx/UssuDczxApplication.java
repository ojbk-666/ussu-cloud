package cc.ussu.modules.dczx;

import cc.ussu.common.security.annotation.EnableCustomConfig;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@EnableFeignClients(basePackages = "cc.ussu")
@MapperScan({"cc.ussu.modules.dczx.**.mapper"})
@EnableCustomConfig
public class UssuDczxApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuDczxApplication.class, args);
    }

}
