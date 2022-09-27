package com.submarket.sellerservice.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BusinessIdApiDto {
    private int request_cnt;
    private String status_code;

    private List<Map<String, Object>> data;
}
