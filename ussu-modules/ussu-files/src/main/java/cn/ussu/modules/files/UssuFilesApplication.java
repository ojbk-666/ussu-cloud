package cn.ussu.modules.files;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class UssuFilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuFilesApplication.class, args);
    }

}
