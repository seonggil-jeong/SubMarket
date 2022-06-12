package com.submarket.itemservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.submarket.itemservice.jpa.ItemRepository;
import com.submarket.itemservice.service.IKafkaConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService implements IKafkaConsumerService {
    /**
     * if there is new Topic --> update DB or something
     */
    private final ItemRepository itemRepository;

    @KafkaListener(topics = "example-item-topic") // Topic Name
    @Override
    public void kafkaTest(String kafkaMessage) {
        // 값은 String 으로 직렬화 하여 넘어옴, Map (JSON) 형식으로 변환해서 사용해야 함
        log.info(this.getClass().getName() + ".kafkaTest");

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }
        log.info("itemSeq : " + map.get("itemSeq"));
    }

}
