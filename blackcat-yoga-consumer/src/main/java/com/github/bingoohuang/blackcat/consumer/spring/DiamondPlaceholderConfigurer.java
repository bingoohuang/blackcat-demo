package com.github.bingoohuang.blackcat.consumer.spring;

import lombok.val;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import static com.github.bingoohuang.blackcat.sdk.utils.Blackcats.readDiamond;

@Component
public class DiamondPlaceholderConfigurer
        extends PropertySourcesPlaceholderConfigurer {
    public DiamondPlaceholderConfigurer() {
        val sources = new MutablePropertySources();
        sources.addFirst(new MapPropertySource("diamond", loadProperties()));
        setPropertySources(sources);
    }

    private Map<String, Object> loadProperties() {
        return (Map<String, Object>) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{Map.class},
                new MapInvocationHandler()
        );
    }

    public static class MapInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            val isGet = method.getName().equals("get");
            return isGet
                    ? readDiamond((String) args[0])
                    : method.invoke(this, args);
        }
    }
}
