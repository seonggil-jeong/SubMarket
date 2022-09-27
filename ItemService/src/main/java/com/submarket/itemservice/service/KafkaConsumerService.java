package com.submarket.itemservice.service;

public interface KafkaConsumerService {
    void reduceItemCount(String kafkaMessage) throws Exception;

    void increaseItemCount(String kafkaMessage) throws Exception;

    void offAllItemBySellerId(String kafkaMessage) throws Exception;
}
