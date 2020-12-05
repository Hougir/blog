package com.yellowhao.Config;

import com.yellowhao.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置类
 */
@Configuration
public class MyConfig extends WebMvcConfigurerAdapter{

    //重写拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/.js")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/login");
    }
}
