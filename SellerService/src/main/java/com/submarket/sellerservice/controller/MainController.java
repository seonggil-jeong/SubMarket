package com.submarket.sellerservice.controller;

import com.submarket.sellerservice.dto.BusinessIdApiDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@RestController
public class MainController {
    private Environment env;

    @Autowired
    public MainController(Environment env) {
        this.env = env;
    }

    @GetMapping("/health")
    public String health() {
        log.info("SellerService On");
        return env.getProperty("spring.application.name")
                + ", port(local.server.port) : " + env.getProperty("local.server.port")
                + ", port(server.port) : " + env.getProperty("server.port")
                + ", token secret : " + env.getProperty("token.secret")
                + ", token expiration time : " + env.getProperty("token.expiration_time");
    }

    @GetMapping("/test")
    public BusinessIdApiDto test() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory()); // no body
        String url = "https://api.odcloud.kr/api/nts-businessman/v1/status";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", env.getProperty("businessId"));

        List<String> bNoList = new LinkedList<>();
        bNoList.add("00000000");

        Map<String, Object> body = new HashMap<>();
        body.put("b_no", bNoList);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);


        ResponseEntity<BusinessIdApiDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, BusinessIdApiDto.class);



        return response.getBody();


    }
}

