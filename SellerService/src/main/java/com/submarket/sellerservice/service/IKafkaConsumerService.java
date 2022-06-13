package com.submarket.sellerservice.service;

public interface IKafkaConsumerService {
    void getOrderInfoFromOrderService(String kafkaMessage) throws Exception;
}
