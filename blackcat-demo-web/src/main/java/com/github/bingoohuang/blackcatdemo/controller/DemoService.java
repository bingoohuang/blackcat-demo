package com.github.bingoohuang.blackcatdemo.controller;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringRestClientEnabled(baseUrl = "http://localhost:4850")
public interface DemoService {
    @RequestMapping("/service")
    String service();
}
