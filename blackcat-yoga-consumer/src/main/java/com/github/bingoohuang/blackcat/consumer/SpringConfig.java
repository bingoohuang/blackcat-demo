package com.github.bingoohuang.blackcat.consumer;

import com.github.bingoohuang.blackcat.consumer.wx.WxMsgService;
import com.github.bingoohuang.blackcat.server.BlackServerEnabled;
import com.github.bingoohuang.blackcat.server.base.MsgService;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientEnabledScan;
import com.github.bingoohuang.utils.redis.Redis;
import com.github.bingoohuang.utils.redis.RedisConfig;
import com.github.bingoohuang.westcache.spring.WestCacheableEnabled;
import lombok.val;
import org.n3r.eql.eqler.spring.EqlerScan;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@SpringRestClientEnabledScan
@WestCacheableEnabled
@EqlerScan
@BlackServerEnabled
public class SpringConfig {
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        val creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public Redis redis() {
        val redisConfig = new RedisConfig();
        redisConfig.setHost("127.0.0.1");
        redisConfig.setPort(6379);
        return new Redis(redisConfig);
    }

    @Bean
    public MsgService msgService() {
        return new WxMsgService();
    }
}
