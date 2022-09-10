package com.submarket.itemservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.submarket.itemservice.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements com.submarket.itemservice.service.KafkaProducerService {
    private final KafkaTemplate kafkaTemplate;

    @Override
    public void sendItemInfoToOrderService(ItemDto itemDto) throws Exception {
        log.info(this.getClass().getName() + ".sendItemInfoToOrderService Start!");
        ObjectMapper mapper = new ObjectMapper();
        String kafkaMessage = "";

        try {
            kafkaMessage = mapper.writeValueAsString(itemDto);
        } catch (JsonProcessingException exception) {
            log.info("JsonProcessingException : " + exception);
            exception.printStackTrace();

        }

        kafkaTemplate.send("sales", kafkaMessage);

        log.info(this.getClass().getName() + ".sendItemInfoToOrderService End!");

    }
}
