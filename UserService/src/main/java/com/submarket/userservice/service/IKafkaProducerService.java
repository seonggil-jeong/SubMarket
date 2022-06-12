package com.submarket.userservice.service;

import com.submarket.userservice.dto.SubDto;

public interface IKafkaProducerService {
    Object send(String kafkaTopic, SubDto subDto) throws Exception;
}
