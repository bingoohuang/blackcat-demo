package com.github.bingoohuang.blackcatdemo;

import com.github.bingoohuang.blackcatdemo.controller.InMemoryMessageRespository;
import com.github.bingoohuang.blackcatdemo.controller.Message;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientEnabledScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

@SpringBootApplication
@SpringRestClientEnabledScan
public class Application {
    @Bean
    public InMemoryMessageRespository messageRepository() {
        return new InMemoryMessageRespository();
    }

    @Bean
    public Converter<String, Message> messageConverter() {
        return new Converter<String, Message>() {
            @Override
            public Message convert(String id) {
                return messageRepository().findMessage(Long.valueOf(id));
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}


