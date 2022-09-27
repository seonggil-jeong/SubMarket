package com.submarket.orderservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String orderId;

    private int itemSeq;

    private String userId;

    private String sellerId;

    private String userAddress;

    private String userAddress2;

    private String orderDateDetails;

    private String orderDate;
}
