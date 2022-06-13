package com.submarket.orderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.submarket.orderservice.dto.OrderDto;
import com.submarket.orderservice.service.IKafkaConsumerService;
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
    private final OrderService orderService;

    @KafkaListener(topics = "sub")
    @Override
    public void kafkaCreateOrder(String kafkaMessage) throws Exception {
        log.info(this.getClass().getName() + ".kafkaCreateOrder Start!");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<String, Object>>() {
            });

        } catch (JsonProcessingException ex) {
            log.info("JsonProcessingException : " + ex);
            ex.printStackTrace();

        }
        OrderDto orderDto = new OrderDto();
        orderDto.setUserId(String.valueOf(map.get("userId")));
        orderDto.setItemSeq(Integer.parseInt(String.valueOf(map.get("itemSeq"))));

        orderService.insertOrder(orderDto);

        log.info(this.getClass().getName() + ".kafkaCreateOrder Start!");
    }
}
