package com.submarket.userservice.service;

import com.submarket.userservice.dto.LikeDto;
import com.submarket.userservice.dto.SubDto;

public interface KafkaProducerService {
    void createNewSub(SubDto subDto) throws Exception;

    void cancelSub(SubDto subDto) throws Exception;


    void createItemLike(LikeDto likeDto) throws Exception;

    void cancelItemLike(LikeDto likeDto) throws Exception;
}
