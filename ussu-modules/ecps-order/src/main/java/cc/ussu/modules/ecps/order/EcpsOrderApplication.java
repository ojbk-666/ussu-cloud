package cc.ussu.modules.ecps.order;

import cc.ussu.common.security.annotation.EnableCustomConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单服务
 */
@EnableCaching
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan({"cc.ussu.modules.ecps.**.mapper"})
@EnableCustomConfig
public class EcpsOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcpsOrderApplication.class, args);
    }

}
