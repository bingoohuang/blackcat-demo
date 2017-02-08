package com.github.bingoohuang.blackcat.consumer;

import com.github.bingoohuang.blackcat.server.handler.CassandraMsgHandler;
import com.github.bingoohuang.blackcat.server.handler.CompositeMsgHandler;
import com.github.bingoohuang.blackcat.server.handler.PrintMsgHandler;
import com.github.bingoohuang.blackcat.sdk.BlackcatMsgHandler;
import com.github.bingoohuang.blackcat.sdk.netty.BlackcatServer;
import com.google.common.collect.Lists;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.val;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        OptionSet options = parseOptions(args);
        val context = new AnnotationConfigApplicationContext(SpringConfig.class);
        val cassandraMsgHandler = context.getBean(CassandraMsgHandler.class);

        List<BlackcatMsgHandler> handlers = Lists.newArrayList();
        if (options.has("print")) handlers.add(new PrintMsgHandler());
        if (options.has("cassandra")) handlers.add(cassandraMsgHandler);

        val composite = new CompositeMsgHandler(handlers);
        new BlackcatServer(composite).startup();
    }

    private static OptionSet parseOptions(String[] args) {
        OptionParser parser = new OptionParser();
        parser.accepts("print");
        parser.accepts("cassandra");

        return parser.parse(args);
    }
}
