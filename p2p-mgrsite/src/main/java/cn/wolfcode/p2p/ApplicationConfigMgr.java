package cn.wolfcode.p2p;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主配置类
 */
@SpringBootApplication
@PropertySource("classpath:application-mgr.properties")
@Import(ApplicationCoreConfig.class)
@EnableScheduling//开启定时器
public class ApplicationConfigMgr
{
    public static void main( String[] args )
    {
        SpringApplication.run(ApplicationConfigMgr.class,args);
    }
}
