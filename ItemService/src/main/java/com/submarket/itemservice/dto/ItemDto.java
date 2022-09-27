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
public class ItemDto {
    private int itemSeq;

    private String sellerId;

    private String itemTitle;

    private String itemContents;

    private int itemPrice;

    private int itemCount; // 상품 수

    private int categorySeq;

    private int itemStatus; // 활성화

    private int readCount20;

    private int readCount30;

    private int readCount40;

    private int readCountOther;

    private int userAge;
    private String mainImagePath; // DB에 저장되어 있는 이미지 정보

    private String subImagePath;

    private int isUserLiked;



    private CategoryEntity category;

    private List<ItemReviewEntity> reviews;

    private MultipartFile mainImage; // Front 에서 넘어온 이미지
    private MultipartFile subImage; // Image 2

}