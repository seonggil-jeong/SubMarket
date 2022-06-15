package com.submarket.sellerservice.service;

import com.submarket.sellerservice.dto.SellerDto;

public interface IKafkaProducerService {
    void kafkaDeleteSeller(SellerDto sellerDto) throws Exception;
}
