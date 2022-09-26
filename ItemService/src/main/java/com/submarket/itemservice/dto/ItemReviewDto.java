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
public class ItemReviewDto {
    private Integer reviewSeq;

    private String userId;

    private String userAge;

    private int reviewStar;

    private String reviewContents;

    private String reviewDate;

    private ItemEntity item;
}
