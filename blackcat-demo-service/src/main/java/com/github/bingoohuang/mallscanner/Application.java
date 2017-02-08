package com.github.bingoohuang.mallscanner;

import org.n3r.eql.eqler.spring.EqlerScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EqlerScan
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


