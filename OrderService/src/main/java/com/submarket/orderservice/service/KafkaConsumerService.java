package com.submarket.orderservice.service;

public interface KafkaConsumerService {
    void kafkaCreateOrder(String kafkaMessage) throws Exception;

    void kafkaGetItemInfoFromItemService(String kafkaMessage) throws Exception;
}
