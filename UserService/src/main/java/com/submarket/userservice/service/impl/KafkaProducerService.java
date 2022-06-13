package com.submarket.userservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.submarket.userservice.dto.SubDto;
import com.submarket.userservice.service.IKafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService implements IKafkaProducerService {
    private final KafkaTemplate kafkaTemplate;

    @Override
    public void createNewSub(SubDto subDto) throws Exception {
        // 구독 생성 시 Sub Topic 에 구독 정보 전송 --> itemService = 상품 수량 감소, orderService = 새로운 구독 정보 생성
        log.info(this.getClass().getName() + ".createNewSub Start!");
        ObjectMapper mapper = new ObjectMapper();
        String kafkaMessage = "";

        try {
            kafkaMessage = mapper.writeValueAsString(subDto);
        } catch (JsonProcessingException ex) {
            log.info("JsonProcessingException : " + ex);
            ex.printStackTrace();
        }

        kafkaTemplate.send("sub", kafkaMessage);
        log.info(this.getClass().getName() + ".createNewSub End!");
    }
}
