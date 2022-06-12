package com.submarket.orderservice.service;

public interface IKafkaConsumerService {
    void kafkaCreateOrder(String kafkaMessage) throws Exception;
}
