package cc.ussu.modules.files;

import cc.ussu.common.swagger.annotation.EnableCustomSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableCustomSwagger2
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class UssuFilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(UssuFilesApplication.class);
    }

}
