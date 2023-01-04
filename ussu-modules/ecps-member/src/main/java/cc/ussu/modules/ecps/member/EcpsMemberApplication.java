package cc.ussu.modules.ecps.member;

import cc.ussu.common.security.annotation.EnableCustomConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan({"cc.ussu.modules.ecps.**.mapper"})
@EnableCustomConfig
public class EcpsMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcpsMemberApplication.class, args);
    }

}
