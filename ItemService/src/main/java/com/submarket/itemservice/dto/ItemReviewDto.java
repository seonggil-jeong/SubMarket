package com.submarket.itemservice.dto;

import com.submarket.itemservice.jpa.entity.ItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "리뷰 정보")
public class ItemReviewDto {
    @Schema(example = "1", required = true, description = "상품 리뷰 번호")
    private Integer reviewSeq;

    @Schema(example = "userId", required = true, description = "사용자 아이디")
    private String userId;

    @Schema(example = "20", required = false, description = "사용자 나이")
    private String userAge;

    @Schema(example = "5", required = false, description = "리뷰 별정")
    private int reviewStar;

    @Schema(example = "reviewContents", required = true, description = "리뷰 내용")
    private String reviewContents;

    @Schema(example = "20220925", required = true, description = "리뷰 작성일")
    private String reviewDate;

    @Schema(example = "ItemEntity", required = true, description = "상품 정보")
    private ItemEntity item;
}
