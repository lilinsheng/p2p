package cn.wolfcode.p2p.base.webconfig;

import cn.wolfcode.p2p.base.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{

    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    /**
     * 注册登录校验拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**");
    }
}
