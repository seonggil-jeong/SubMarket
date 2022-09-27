package com.submarket.sellerservice.controller;

import com.submarket.sellerservice.dto.BusinessIdApiDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Health API", description = "서버 상태 확인")
public class MainController {
    private final Environment env;



    @Operation(summary = "상태 확인", description = "인증 정보 없이 접근할 수 있는 EndPoint")
    @ApiResponses({
            @ApiResponse(responseCode = "503", description = "Eureka 서버에 미 등록"),
            @ApiResponse(responseCode = "401", description = "Token 인증 실패"),
            @ApiResponse(responseCode = "403", description = "Spring Security 인증 실패")

    })
    @GetMapping("/health")
    public String health() {
        log.info("SellerService On");
        return env.getProperty("spring.application.name")
                + ", port(local.server.port) : " + env.getProperty("local.server.port")
                + ", port(server.port) : " + env.getProperty("server.port")
                + ", token secret : " + env.getProperty("token.secret")
                + ", token expiration time : " + env.getProperty("token.expiration_time");
    }
}

