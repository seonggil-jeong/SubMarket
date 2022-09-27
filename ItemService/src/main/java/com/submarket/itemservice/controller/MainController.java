package com.submarket.itemservice.controller;

import com.submarket.itemservice.service.impl.SchedulerServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final Environment env;
    private final SchedulerServiceImpl schedulerServiceImpl;

    @GetMapping("/health")
    public String health() {
        log.info("ItemService On");
        return env.getProperty("spring.application.name")
                + ", port(local.server.port) : " + env.getProperty("local.server.port")
                + ", port(server.port) : " + env.getProperty("server.port")
                + ", token secret : " + env.getProperty("token.secret")
                + ", token expiration time : " + env.getProperty("token.expiration_time");
    }

    @GetMapping("/test")
    public void test() throws Exception{
        schedulerServiceImpl.createSellerTotalSales();
    }

}
