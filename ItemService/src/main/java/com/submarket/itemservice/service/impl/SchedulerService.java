package com.submarket.itemservice.service.impl;

import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.jpa.ItemRepository;
import com.submarket.itemservice.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {
    private final KafkaProducerService kafkaProducerService;
    private final ItemService itemService;
    @Scheduled(cron = "0 15 10 L * ?") // 매월 말 일 실행
    @Async
    public void createSellerTotalSales() throws Exception {
        log.info(this.getClass().getName() + ".createSellerTotalSales Start!");

        List<ItemDto> itemDtoList = itemService.findAllItem();
        log.info("itemDtoList Size : " + itemDtoList.size());
        for (ItemDto itemDto : itemDtoList) {
            kafkaProducerService.sendItemInfoToOrderService(itemDto);
        }

        log.info(this.getClass().getName() + ".createSellerTotalSales End!");

    }
}
