package com.submarket.itemservice.dto;

import com.submarket.itemservice.jpa.entity.ItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Category 정보")
public class CategoryDto {
    @Schema(example = "1", required = true, description = "Category 번호")
    private int categorySeq; // 1, 2

    @Schema(example = "식품", required = true, description = "Category 이름")
    private String categoryName; // 식품, 음료

    @Schema(example = "List<ItemEntity>", required = false, description = "상품 정보 목록")
    private List<ItemEntity> items;
}
