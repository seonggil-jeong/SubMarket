package com.submarket.userservice.dto;

import com.submarket.userservice.jpa.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubDto {
    private Integer subSeq;
    private int itemSeq;
    private String subDate;
    private int subCount;
    private UserEntity user;
    private String userId;


    // Kafka Setting
    private String sellerId;
    private String userAddress;
    private String userAddress2;
    private String userAge;
}
