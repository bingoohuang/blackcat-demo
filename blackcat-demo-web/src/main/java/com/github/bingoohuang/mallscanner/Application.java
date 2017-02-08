package com.github.bingoohuang.mallscanner;

import com.github.bingoohuang.mallscanner.controller.InMemoryMessageRespository;
import com.github.bingoohuang.mallscanner.controller.Message;
import com.github.bingoohuang.mallscanner.controller.MessageRepository;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientEnabledScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

@SpringBootApplication
@SpringRestClientEnabledScan
public class Application {
    @Bean
    public MessageRepository messageRepository() {
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


