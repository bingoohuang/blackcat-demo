package com.github.bingoohuang.blackcat.consumer;

import com.github.bingoohuang.blackcat.server.BlackServerEnabled;
import com.github.bingoohuang.blackcat.server.base.MsgService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@BlackServerEnabled
public class SpringConfig {
    @Bean
    public MsgService msgService() {
        return new MsgService() {
            @Override public void sendMsg(String title, String detail) {

            }
        };
    }
}
