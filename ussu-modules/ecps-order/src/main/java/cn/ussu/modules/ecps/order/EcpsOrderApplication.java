package cn.ussu.modules.ecps.order;

import cn.ussu.common.security.config.FeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * 订单服务
 */
@EnableCaching
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan({"cn.ussu.modules.ecps.**.mapper"})
@Import(FeignConfig.class)
public class EcpsOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcpsOrderApplication.class, args);
    }

}
