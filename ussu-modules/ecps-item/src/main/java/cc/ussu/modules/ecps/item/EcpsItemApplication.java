package cc.ussu.modules.ecps.item;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCaching
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@MapperScan({"cc.ussu.modules.ecps.**.mapper"})
public class EcpsItemApplication extends SpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcpsItemApplication.class, args);
    }

}
