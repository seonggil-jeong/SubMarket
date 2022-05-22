package com.submarket.userservice.vo;

import com.submarket.userservice.jpa.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSub {
    private Integer subSeq;
    private int itemSeq;
    private String subDate;
    private int subCount;
    private UserEntity user;
}
