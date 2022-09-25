package com.submarket.itemservice.dto;

import com.submarket.itemservice.jpa.entity.CategoryEntity;
import com.submarket.itemservice.jpa.entity.ItemReviewEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "상품 정보")
public class ItemDto {
    @Schema(example = "1", required = true, description = "상품 번호")
    private int itemSeq;

    @Schema(example = "sellerId", required = true, description = "판매자 아이디")
    private String sellerId;

    @Schema(example = "itemTitle", required = true, description = "상품 이름 정보")
    private String itemTitle;

    @Schema(example = "itemContents", required = true, description = "상품 내용")
    private String itemContents;

    @Schema(example = "20000", required = true, description = "상품 가격")
    private int itemPrice;

    @Schema(example = "100", required = true, description = "상품 수량")
    private int itemCount; // 상품 수

    @Schema(example = "1", required = true, description = "상품 카테고리 번호")
    private int categorySeq;

    @Schema(example = "1", required = true, description = "상품 활성화 여부", defaultValue = "1")
    private int itemStatus; // 활성화

    @Schema(example = "1", required = false, description = "20대 사용자 상품 조회 수", defaultValue = "0")
    private int readCount20;

    @Schema(example = "1", required = false, description = "30대 사용자 상품 조회 수", defaultValue = "0")
    private int readCount30;

    @Schema(example = "1", required = false, description = "40대 사용자 상품 조회 수", defaultValue = "0")
    private int readCount40;

    @Schema(example = "1", required = false, description = "나이 미 입력 또는 필터링 불가능한 사용자 상품 조회 수", defaultValue = "0")
    private int readCountOther;

    private int userAge;
    private String mainImagePath; // DB에 저장되어 있는 이미지 정보

    private String subImagePath;

    @Schema(example = "1", description = "사용자 상품 좋아요 유무", defaultValue = "0")
    private int isUserLiked;



    private CategoryEntity category;

    private List<ItemReviewEntity> reviews;

    private MultipartFile mainImage; // Front 에서 넘어온 이미지
    private MultipartFile subImage; // Image 2

}