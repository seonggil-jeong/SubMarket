package com.submarket.orderservice.service;

public interface KafkaProducerService {
    void kafkaSendPriceToSellerService(int totalPrice, String date, String sellerId) throws Exception;
}
