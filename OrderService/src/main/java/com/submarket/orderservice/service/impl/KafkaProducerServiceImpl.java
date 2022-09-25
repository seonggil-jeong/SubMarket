package com.submarket.orderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.submarket.orderservice.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate kafkaTemplate;

    @Override
    public void kafkaSendPriceToSellerService(int totalPrice, String date, String sellerId) throws Exception {
        log.info(this.getClass().getName() + ".kafkaSendPriceToSellerService Start!");
        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        String kafkaMessage = "";
        map.put("totalPrice", totalPrice);
        map.put("date", date);
        map.put("sellerId", sellerId);

        try {
            kafkaMessage = mapper.writeValueAsString(map);
            log.info("kafkaMessage : " + kafkaMessage);

        } catch (JsonProcessingException exception) {
            log.info("JsonProcessingException : " + exception);
            exception.printStackTrace();
        }

        kafkaTemplate.send("order", kafkaMessage);

        log.info(this.getClass().getName() + ".kafkaSendPriceToSellerService End!");
    }
}
