package cc.ussu.modules.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        System.out.println(0);
        SpringApplication app = new SpringApplication(TestApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }

}
