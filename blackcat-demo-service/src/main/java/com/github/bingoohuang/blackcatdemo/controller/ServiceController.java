package com.github.bingoohuang.blackcatdemo.controller;

import com.github.bingoohuang.blackcat.instrument.annotations.BlackcatMonitor;
import com.github.bingoohuang.blackcat.instrument.callback.Blackcat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.UUID;

@RestController
@BlackcatMonitor
@Slf4j
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
        int millis = random.nextInt(200);
        log.debug("1st sleep for {} millis", millis);
        Thread.sleep(millis);
        Blackcat.log("service 步骤2");
        int millis2 = random.nextInt(200);
        log.debug("2nd sleep for {} millis", millis2);
        Thread.sleep(millis2);
        demoDao.demo(UUID.randomUUID().toString(), Blackcat.currentTraceId());
        return "service response body";
    }
}