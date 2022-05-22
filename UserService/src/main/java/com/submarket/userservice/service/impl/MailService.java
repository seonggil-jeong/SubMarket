package com.submarket.userservice.service.impl;

import com.submarket.userservice.jpa.SubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "MailService")
@RequiredArgsConstructor
public class MailService {
    private final Environment env;
    private final JavaMailSender javaMailSender;

    @Async
    public void sendMail(String mailAddress, String title, String mailMessage) {
        log.info(this.getClass().getName() + ".SendMail Start!");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(env.getProperty("spring.mail.username"));
        message.setTo(mailAddress);
        message.setSubject(title);
        message.setText(mailMessage);

        javaMailSender.send(message);

        log.info(this.getClass().getName() + ".SendMail End!");
    }

}