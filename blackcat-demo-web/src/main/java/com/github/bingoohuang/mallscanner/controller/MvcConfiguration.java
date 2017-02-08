package com.github.bingoohuang.mallscanner.controller;

import com.github.bingoohuang.blackcat.javaagent.spring.BlackcatInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BlackcatInterceptor());
        registry.addInterceptor(new DemoInterceptor());
    }

}
