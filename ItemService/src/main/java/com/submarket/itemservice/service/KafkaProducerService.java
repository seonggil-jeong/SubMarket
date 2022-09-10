package com.submarket.itemservice.service;

import com.submarket.itemservice.dto.ItemDto;

public interface KafkaProducerService {
    void sendItemInfoToOrderService(ItemDto itemDto) throws Exception;

}
