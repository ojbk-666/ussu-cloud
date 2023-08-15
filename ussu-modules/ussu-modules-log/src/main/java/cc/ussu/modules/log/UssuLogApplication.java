package cc.ussu.modules.log;

import cc.ussu.common.swagger.annotation.EnableCustomSwagger2;
import cn.easyes.starter.register.EsMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCustomSwagger2
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = "cc.ussu")
@EsMapperScan({"cc.ussu.modules.log.es.mapper"})
public class UssuLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuLogApplication.class);
    }

}
