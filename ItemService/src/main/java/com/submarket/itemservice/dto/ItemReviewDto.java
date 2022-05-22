package com.submarket.itemservice.dto;

import com.submarket.itemservice.jpa.entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemReviewDto {
    private Integer reviewSeq;
    private int userSeq;
    private int reviewStar;
    private String reviewContents;
    private String reviewDate;
    private ItemEntity item;
}
