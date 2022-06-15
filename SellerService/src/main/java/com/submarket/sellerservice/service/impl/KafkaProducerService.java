package com.submarket.sellerservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.submarket.sellerservice.dto.SellerDto;
import com.submarket.sellerservice.service.IKafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService implements IKafkaProducerService {
    private final KafkaTemplate kafkaTemplate;

    @Override
    public void kafkaDeleteSeller(SellerDto sellerDto) throws Exception {
        log.info(this.getClass().getName() + ".kafkaDeleteSeller Start!");
        ObjectMapper mapper = new ObjectMapper();
        String kafkaMessage = "";

        try {

            kafkaMessage = mapper.writeValueAsString(sellerDto);

        } catch (JsonProcessingException exception) {
            log.info("JsonProcessingException : " + exception);
            exception.printStackTrace();

        }

        kafkaTemplate.send("seller", kafkaMessage);

        log.info(this.getClass().getName() + ".kafkaDeleteSeller End!");

    }
}
