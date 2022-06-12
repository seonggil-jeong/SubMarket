package com.submarket.userservice.controller;

import com.submarket.userservice.dto.SubDto;
import com.submarket.userservice.dto.UserDto;
import com.submarket.userservice.jpa.SubRepository;
import com.submarket.userservice.jpa.UserRepository;
import com.submarket.userservice.jpa.entity.UserEntity;
import com.submarket.userservice.mapper.UserMapper;
import com.submarket.userservice.service.impl.KafkaProducerService;
import com.submarket.userservice.service.impl.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {
    private final Environment env;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final SubRepository subRepository;
    private final KafkaProducerService kafkaProducerService;


    @GetMapping("/health")
    public String health() {
        log.info("UserService On");
        return env.getProperty("spring.application.name")
                + ", port(local.server.port) : " + env.getProperty("local.server.port")
                + ", port(server.port) : " + env.getProperty("server.port")
                + ", token secret : " + env.getProperty("token.secret")
                + ", token expiration time : " + env.getProperty("token.expiration_time");
    }

    @GetMapping("/test")
    @Transactional
    public UserDto test() throws Exception {
        SubDto subDto = new SubDto();
        subDto.setItemSeq(30);
        kafkaProducerService.send("example-item-topic", subDto);

        return null;
    }
}
