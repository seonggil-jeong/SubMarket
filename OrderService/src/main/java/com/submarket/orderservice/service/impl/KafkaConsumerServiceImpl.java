package com.submarket.orderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.submarket.orderservice.dto.OrderDto;
import com.submarket.orderservice.service.KafkaConsumerService;
import com.submarket.orderservice.util.CmmUtil;
import com.submarket.orderservice.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerServiceImpl implements KafkaConsumerService {
    private final OrderServiceImpl orderServiceImpl;
    private final KafkaProducerServiceImpl kafkaProducerServiceImpl;

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

        orderServiceImpl.insertOrder(orderDto);

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
        int itemPrice = Integer.parseInt(String.valueOf(map.get("itemPrice")));
        int itemSeq = Integer.parseInt(String.valueOf(map.get("itemSeq")));
        String sellerId = String.valueOf(map.get("sellerId"));

        log.info("itemPrice : " + itemPrice);

        OrderDto orderDto = new OrderDto();
        orderDto.setItemSeq(itemSeq);

        int totalPrice = orderServiceImpl.totalPriceByItemSeq(orderDto, itemPrice);
        String date = DateUtil.getDateTime("yyyyMM");

        kafkaProducerServiceImpl.kafkaSendPriceToSellerService(totalPrice, date, sellerId);


        log.info(this.getClass().getName() + ".kafkaGetItemInfoFromItemService End!");
    }
}
