package com.submarket.sellerservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.submarket.sellerservice.dto.SalesDto;
import com.submarket.sellerservice.service.IKafkaConsumerService;
import com.submarket.sellerservice.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService implements IKafkaConsumerService {
    private final KafkaTemplate kafkaTemplate;
    private final SalesService salesService;

    @KafkaListener(topics = "order")
    @Override
    public void getOrderInfoFromOrderService(String kafkaMessage) throws Exception {
        log.info(this.getClass().getName() + ".getOrderInfoFromOrderService Start!");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException exception) {
            log.info("JsonProcessingException : " + exception);
            exception.printStackTrace();

        }

        String sellerId = String.valueOf(map.get("sellerId"));
        int totalPrice = Integer.parseInt(String.valueOf(map.get("totalPrice")));
        String date = String.valueOf(map.get("date"));

        log.info("sellerId : " + sellerId);
        log.info("totalPrice : " + totalPrice);
        log.info("date : " + date);

        SalesDto salesDto = new SalesDto();
        salesDto.setDate(date);
        salesDto.setValue(totalPrice);

        salesService.saveSales(salesDto, sellerId);

        log.info(this.getClass().getName() + ".getOrderInfoFromOrderService End!");

    }
}
