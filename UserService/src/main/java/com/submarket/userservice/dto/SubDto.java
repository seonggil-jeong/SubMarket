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

    private int userSeq;
}
