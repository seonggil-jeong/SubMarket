package com.submarket.userservice.controller;

import com.submarket.userservice.dto.UserDto;
import com.submarket.userservice.jpa.LikeRepository;
import com.submarket.userservice.jpa.SubRepository;
import com.submarket.userservice.jpa.UserRepository;
import com.submarket.userservice.jpa.entity.LikeEntity;
import com.submarket.userservice.service.impl.KafkaProducerServiceImpl;
import com.submarket.userservice.service.impl.MailServiceImpl;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {
    private final Environment env;
    private final MailServiceImpl mailServiceImpl;
    private final UserRepository userRepository;
    private final SubRepository subRepository;
    private final KafkaProducerServiceImpl kafkaProducerServiceImpl;
    private final LikeRepository likeRepository;


    @GetMapping("/health")
    // logging {name}
    @Timed(value = "user.health", longTask = true)
    public String health() {
        log.info("UserService On");
        return env.getProperty("spring.application.name")
                + ", port(local.server.port) : " + env.getProperty("local.server.port")
                + ", port(server.port) : " + env.getProperty("server.port")
                + ", token secret : " + env.getProperty("token.secret")
                + ", token expiration time : " + env.getProperty("token.expiration_time");
    }
}
