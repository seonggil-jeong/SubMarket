package com.submarket.orderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.submarket.orderservice.dto.OrderDto;
import com.submarket.orderservice.service.IKafkaConsumerService;
import com.submarket.orderservice.util.CmmUtil;
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
    private final OrderService orderService;
    private final KafkaTemplate kafkaTemplate;

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
        orderDto.setSellerId(String.valueOf(map.get("sellerId")));
        orderDto.setUserAddress(String.valueOf(map.get("userAddress")));
        orderDto.setUserAddress2(CmmUtil.nvl(String.valueOf(map.get("userAddress2"))));

        orderService.insertOrder(orderDto);

        log.info(this.getClass().getName() + ".kafkaCreateOrder Start!");
    }
    
    @KafkaListener(topics = "sales")
    @Override
    public void kafkaGetItemInfoFromItemService(String kafkaMessage) throws Exception {
        log.info(this.getClass().getName() + ".kafkaGetItemInfoFromItemService Start!");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException exception) {
            log.info("JsonProcessingException : " + exception);
            exception.printStackTrace();
        }

        // TODO: 2022/06/14 date and value return to sellerService
        kafkaTemplate.send("order", kafkaMessage);

        log.info(this.getClass().getName() + ".kafkaGetItemInfoFromItemService End!");
    }
}
