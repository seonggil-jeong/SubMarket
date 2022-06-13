package com.submarket.itemservice.service;

public interface IKafkaConsumerService {
    void reduceItemCount(String kafkaMessage) throws Exception;

    void increaseItemCount(String kafkaMessage) throws Exception;
}
