package com.submarket.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private String orderId;
    private int itemSeq;
    private String userId;
    private String sellerId;

    private String userAddress;
    private String userAddress2;

    private String orderDateDetails;
    private String orderDate;
}
