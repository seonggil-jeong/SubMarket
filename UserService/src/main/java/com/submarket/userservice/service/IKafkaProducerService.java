package com.submarket.userservice.service;

import com.submarket.userservice.dto.SubDto;

public interface IKafkaProducerService {
    void createNewSub(SubDto subDto) throws Exception;
}
