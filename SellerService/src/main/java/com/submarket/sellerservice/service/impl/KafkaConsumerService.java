package com.submarket.sellerservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @KafkaListener(topics = "order")
    @Override
    public void getOrderInfoFromOrderService(String kafkaMessage) throws Exception {
        log.info(this.getClass().getName() + ".getOrderInfoFromOrderService Start!");


        // TODO: 2022/06/14 매출 정보 Update And Flask Service 호출
        log.info(this.getClass().getName() + ".getOrderInfoFromOrderService End!");

    }
}
