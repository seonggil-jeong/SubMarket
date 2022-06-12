package com.submarket.itemservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.submarket.itemservice.jpa.ItemRepository;
import com.submarket.itemservice.jpa.entity.ItemEntity;
import com.submarket.itemservice.service.IKafkaConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService implements IKafkaConsumerService {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    /**
     * if there is new Topic --> update DB or something
     */

    @KafkaListener(topics = "sub") // Topic Name
    @Override
    @Transactional
    public void reduceItemCount(String kafkaMessage) {
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

        Optional<ItemEntity> itemEntityOptional = itemRepository.findById(itemSeq);
        if (itemEntityOptional.isPresent()) {
            itemRepository.reduceItemCount(itemSeq);
        }
    }

}
