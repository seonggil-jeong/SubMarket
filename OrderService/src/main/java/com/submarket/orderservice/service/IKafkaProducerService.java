package com.submarket.orderservice.service;

public interface IKafkaProducerService {
    void kafkaSendPriceToSellerService(int totalPrice, String date, String sellerId) throws Exception;
}
