package com.github.bingoohuang.blackcatdemo.controller;

import com.github.bingoohuang.blackcat.instrument.annotations.BlackcatMonitor;
import com.github.bingoohuang.blackcat.instrument.callback.Blackcat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.UUID;

@RestController
@BlackcatMonitor
public class ServiceController {
    @Autowired DemoDao demoDao;

    @PostConstruct
    public void postConstruct() {
        demoDao.createTable();
    }

    @RequestMapping("/service")
    public String service() throws InterruptedException {
        Blackcat.log("service 步骤1");
        Random random = new Random();
        Thread.sleep(random.nextInt(200));
        Blackcat.log("service 步骤2");
        Thread.sleep(random.nextInt(200));
        demoDao.demo(UUID.randomUUID().toString(), Blackcat.currentTraceId());
        return "service response body";
    }
}