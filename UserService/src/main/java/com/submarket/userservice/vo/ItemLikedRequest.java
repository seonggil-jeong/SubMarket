package com.submarket.userservice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request - 상품 정보 좋아요 요청")
public class ItemLikedRequest {
    @NotNull
    @Schema(required = true, description = "상품 번호", example = "1")
    private Integer itemSeq;
}
