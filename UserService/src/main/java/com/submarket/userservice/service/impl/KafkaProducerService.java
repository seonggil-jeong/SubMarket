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
    public Object send(String kafkaTopic, SubDto subDto) throws Exception {
        // 값을 Topic 에 보낼 때는 직렬화 하여 (String) 형식으로 만들어 보내줘야 함
        log.info(this.getClass().getName() + ".send with Kafka Start!");

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(subDto);

        } catch (JsonProcessingException e) {
            log.info("JsonProcessingException : " + e);
            e.printStackTrace();

        }
        kafkaTemplate.send(kafkaTopic, jsonInString);
        log.info("Kafka Producer send data to (" + kafkaTopic + ") : " + subDto);

        return subDto;
    }
}
