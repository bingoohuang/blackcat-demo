package com.github.bingoohuang.springbootbank.demo;

import com.github.bingoohuang.blackcat.javaagent.annotations.BlackcatCreateTransformedClassFile;
import com.github.bingoohuang.blackcat.javaagent.callback.Blackcat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@RestController
@BlackcatCreateTransformedClassFile
public class ServiceController {
    @Autowired DemoDao demoDao;

    @RequestMapping("/service")
    public String service() throws InterruptedException {
        Blackcat.log("service 步骤1");
//        Random random = new Random();
//        Thread.sleep(random.nextInt(1000));
        Blackcat.log("service 步骤2");
//        Thread.sleep(random.nextInt(1000));
        demoDao.demo(UUID.randomUUID().toString(), Blackcat.currentTraceId());
        return "service response body";
    }
}