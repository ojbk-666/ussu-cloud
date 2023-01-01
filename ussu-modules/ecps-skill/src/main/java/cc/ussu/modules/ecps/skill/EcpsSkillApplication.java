package cc.ussu.modules.ecps.skill;

import cc.ussu.common.security.config.FeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan({"cc.ussu.modules.ecps.**.mapper"})
@Import(FeignConfig.class)
public class EcpsSkillApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcpsSkillApplication.class, args);
    }

}
