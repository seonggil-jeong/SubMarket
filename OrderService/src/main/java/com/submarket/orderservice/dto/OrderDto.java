package com.submarket.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private String orderId;
    private int itemSeq;
    private String userId;
    private String sellerId;

    private String orderDate;
}
