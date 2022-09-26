package com.submarket.itemservice.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemInfoResponse {
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
    private String mainImagePath; // DB에 저장되어 있는 이미지 정보
    private String subImagePath;
}
