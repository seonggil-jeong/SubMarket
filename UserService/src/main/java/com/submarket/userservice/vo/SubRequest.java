package com.submarket.userservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request - 상품 구독")
public class SubRequest {

    @Schema(required = true, description = "상품 번호")
    private int itemSeq;

    @Schema(description = "구독 번호")
    private int subSeq;

    @Schema(description = "사용자 번호")
    private int userSeq;
}
