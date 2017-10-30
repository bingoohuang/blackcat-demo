package com.github.bingoohuang.blackcat.consumer;

import com.github.bingoohuang.blackcat.sdk.BlackcatMsgHandler;
import com.github.bingoohuang.blackcat.sdk.netty.BlackcatServer;
import com.github.bingoohuang.blackcat.server.handler.CassandraMsgHandler;
import com.github.bingoohuang.blackcat.server.handler.CompositeMsgHandler;
import com.github.bingoohuang.blackcat.server.handler.PrintMsgHandler;
import com.google.common.collect.Lists;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.val;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) throws Exception {
        val handlers = Lists.<BlackcatMsgHandler>newArrayList();

        val options = parseOptions(args);
        if (options.has("print")) {
            handlers.add(new PrintMsgHandler());
        }
        if (options.has("cassandra")) {
            val context = new AnnotationConfigApplicationContext(SpringConfig.class);
            val cassandraMsgHandler = context.getBean(CassandraMsgHandler.class);
            handlers.add(cassandraMsgHandler);
        }

        val composite = new CompositeMsgHandler(handlers);
        new BlackcatServer(composite).startup();
    }

    private static OptionSet parseOptions(String[] args) {
        val parser = new OptionParser();
        parser.accepts("print");
        parser.accepts("cassandra");

        return parser.parse(args);
    }
}
