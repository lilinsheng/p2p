package cn.wolfcode.p2p;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Import(ApplicationCoreConfig.class)
@PropertySource("classpath:application-web.properties")

public class ApplicationConfigWeb {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfigWeb.class,args);
    }
}
