package com.submarket.userservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.submarket.userservice.dto.LikeDto;
import com.submarket.userservice.dto.SubDto;
import com.submarket.userservice.exception.UserException;
import com.submarket.userservice.exception.result.UserExceptionResult;
import com.submarket.userservice.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.submarket.userservice.constants.KafkaConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final KafkaTemplate kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void createNewSub(SubDto subDto) throws Exception {
        // 구독 생성 시 Sub Topic 에 구독 정보 전송 --> itemService = 상품 수량 감소, orderService = 새로운 구독 정보 생성
        log.debug(this.getClass().getName() + ".createNewSub Start!");
        String kafkaMessage = subDtoToString(subDto);

        kafkaTemplate.send(TOPIC_NAME_SUB, kafkaMessage);
        log.info(this.getClass().getName() + ".createNewSub End!");
    }

    @Override
    public void cancelSub(SubDto subDto) throws Exception {
        log.debug(this.getClass().getName() + ".cancelSub Start!");
        String kafkaMessage = subDtoToString(subDto);

        kafkaTemplate.send(TOPIC_NAME_CANCEL_SUB, kafkaMessage);

        log.info(this.getClass().getName() + ".cancelSub End!");
    }

    private String subDtoToString(final SubDto subDto) throws Exception {
        try {
            return objectMapper.writeValueAsString(subDto);

        } catch (JsonProcessingException exception) {
            log.info("JsonProcessingException : " + exception);
            exception.printStackTrace();

            throw new UserException(UserExceptionResult.CAN_NOT_CREATE_KAFKA_TOPIC);

        }

    }


    /**
     * 상품 좋아요 성공 -> (새로운 좋아요 생성 시) itemService 에 정보 전송
     *
     * @param likeDto
     * @throws Exception
     */
    @Override
    public void createItemLike(final LikeDto likeDto) throws Exception {
        log.debug(this.getClass().getName() + ".createItemLike Start!");
        String kafkaMessage = likeDtoToString(likeDto);

        kafkaTemplate.send(TOPIC_NAME_ITEM_LIKED, kafkaMessage);

        log.info(this.getClass().getName() + ".createNewSub End!");

    }

    /**
     * 상품 좋아요 취소 -> (이미 좋아요 상태 시) itemService 에 전송
     *
     * @param likeDto
     * @throws Exception
     */

    @Override
    public void cancelItemLike(LikeDto likeDto) throws Exception {
        log.debug(this.getClass().getName() + ".cancelItemLike Start!");
        String kafkaMessage = likeDtoToString(likeDto);

        kafkaTemplate.send(TOPIC_NAME_CANCEL_ITEM_LIKED, kafkaMessage);

        log.info(this.getClass().getName() + ".createNewSub End!");

    }


    private String likeDtoToString(final LikeDto likeDto) throws Exception { // Dto -> String
        try {
            return objectMapper.writeValueAsString(likeDto);
        } catch (JsonProcessingException ex) {
            log.info("JsonProcessingException : " + ex);
            ex.printStackTrace();
            throw new UserException(UserExceptionResult.CAN_NOT_CREATE_KAFKA_TOPIC);
        }
    }

}
