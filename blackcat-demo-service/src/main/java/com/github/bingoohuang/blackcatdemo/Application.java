package com.github.bingoohuang.blackcatdemo;

import org.n3r.eql.eqler.spring.EqlerScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EqlerScan
@SpringBootApplication
public class Application {
    public static void main(String[] args) throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("shutdown hook");
            }
        });
        SpringApplication.run(Application.class, args);
    }
}


