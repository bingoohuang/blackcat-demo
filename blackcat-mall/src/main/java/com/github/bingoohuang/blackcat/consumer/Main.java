package com.github.bingoohuang.blackcat.consumer;

import com.github.bingoohuang.blackcat.sdk.BlackcatMsgHandler;
import com.github.bingoohuang.blackcat.sdk.netty.BlackcatServer;
import com.github.bingoohuang.blackcat.server.handler.CassandraMsgHandler;
import com.github.bingoohuang.blackcat.server.handler.CompositeMsgHandler;
import com.google.common.collect.Lists;
import lombok.val;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        val context = new AnnotationConfigApplicationContext(SpringConfig.class);
        val cassandraMsgHandler = context.getBean(CassandraMsgHandler.class);

        List<BlackcatMsgHandler> handlers = Lists.newArrayList();
        handlers.add(cassandraMsgHandler);

        val composite = new CompositeMsgHandler(handlers);
        new BlackcatServer(composite).startup();
    }

}
