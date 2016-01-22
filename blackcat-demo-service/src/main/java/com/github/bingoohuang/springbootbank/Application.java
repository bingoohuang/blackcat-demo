package com.github.bingoohuang.springbootbank;

import org.n3r.eql.eqler.spring.EqlerScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EqlerScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}


