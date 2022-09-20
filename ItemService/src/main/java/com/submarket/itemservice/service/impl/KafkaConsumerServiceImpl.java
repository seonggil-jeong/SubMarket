package com.submarket.itemservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.jpa.ItemRepository;
import com.submarket.itemservice.jpa.entity.ItemEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerServiceImpl implements com.submarket.itemservice.service.KafkaConsumerService {
    private final ItemServiceImpl itemService;
    private final ItemRepository itemRepository;
    /**
     * if there is new Topic --> update DB or something
     */

    @KafkaListener(topics = "sub") // Topic Name
    @Override
    @Transactional
    public void reduceItemCount(String kafkaMessage) throws Exception{
        // 값은 String 으로 직렬화 하여 넘어옴, Map (JSON) 형식으로 변환해서 사용해야 함
        log.info(this.getClass().getName() + ".reduceItemCount Start");

        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<String, Object>>() {});

        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }

        int itemSeq = Integer.parseInt(String.valueOf(map.get("itemSeq")));
        int userAge = Integer.parseInt(String.valueOf(map.get("userAge")));

        log.info("itemSeq : " + itemSeq);

        Optional<ItemEntity> itemEntityOptional = itemRepository.findById(itemSeq);
        if (itemEntityOptional.isPresent()) {
            itemRepository.reduceItemCount(itemSeq);
            itemService.upReadCount(itemSeq, userAge, 30); // 구독 시 ReadCount += 30
        }
    }

    @KafkaListener(topics = "sub-cancel")
    @Override
    @Transactional
    public void increaseItemCount(String kafkaMessage) throws Exception {
        log.info(this.getClass().getName() + ".cancelSub Start!");

        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<String, Object>>() {
            });

        } catch (JsonProcessingException exception) {
            log.info("JsonProcessingException : " + exception);
            exception.printStackTrace();

        }

        int itemSeq = Integer.parseInt(String.valueOf(map.get("itemSeq")));
        itemRepository.increaseItemCount(itemSeq);


        log.info(this.getClass().getName() + ".cancelSub End!");
    }

    @KafkaListener(topics = "seller")
    @Override
    @Transactional
    public void offAllItemBySellerId(String kafkaMessage) throws Exception {
        log.info(this.getClass().getName() + ".offAllItemBySellerId Start!");
        Map<String, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<String, Object>>() {

            });
        } catch (JsonProcessingException exception) {
            log.info("JsonProcessingException : " + exception);
            exception.printStackTrace();
        }

        String sellerId = String.valueOf(map.get("sellerId"));

        if (sellerId != null) {
            List<ItemDto> itemDtoList = itemService.findItemBySellerId(sellerId);

            for (ItemDto itemDto : itemDtoList) {
                log.info("itemSeq : " + itemDto.getItemSeq());
                itemService.offItem(itemDto);
            }
        }


        log.info(this.getClass().getName() + ".offAllItemBySellerId End!");

    }
}
