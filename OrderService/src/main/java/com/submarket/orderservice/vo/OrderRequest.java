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
@Schema(description = "Request - 주문 정보")
public class OrderRequest {
    @Schema(example = "{UUID}", description = "주문 번호")
    private String orderId;

    @Schema(example = "1", description = "상품 번호", required = true)
    private int itemSeq;

    @Schema(example = "userId", description = "사용자 아이디", required = true)
    private String userId;

    @Schema(example = "sellerId", description = "판매자 아이디", required = true)
    private String sellerId;

    @Schema(example = "userAddress", description = "사용자 주소 정보", required = true)
    private String userAddress;

    @Schema(example = "userAddress2", description = "사용자 상세 주소")
    private String userAddress2;

    @Schema(example = "Tue Sep 20 20:49:35 KST 2022", description = "주문 시간")
    private String orderDateDetails;

    @Schema(example = "202209", description = "주문일 날짜 정보를 가지고 작업 시 사용")
    private String orderDate;
}
