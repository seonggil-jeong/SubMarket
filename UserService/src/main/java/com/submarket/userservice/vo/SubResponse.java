package com.submarket.userservice.vo;

import com.submarket.userservice.jpa.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubResponse {

    private Integer subSeq;
    private int itemSeq;
    private String subDate;
    private int subCount;
    private UserEntity user;
}
