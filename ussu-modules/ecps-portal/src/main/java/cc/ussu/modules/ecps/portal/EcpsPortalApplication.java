package cc.ussu.modules.ecps.portal;

import cc.ussu.common.security.annotation.EnableCustomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCaching
@EnableFeignClients
@EnableDiscoveryClient
// @SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableCustomConfig
// @MapperScan({"cc.ussu.modules.ecps.**.mapper"})
public class EcpsPortalApplication extends SpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcpsPortalApplication.class, args);
    }

}
