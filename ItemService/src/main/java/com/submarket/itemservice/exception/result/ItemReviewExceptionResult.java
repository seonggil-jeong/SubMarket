package com.submarket.itemservice.exception.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ItemReviewExceptionResult {
    ITEM_REVIEW_IS_NULL(HttpStatus.BAD_REQUEST, "리뷰 정보를 입력해주세요"),
    ITEM_REVIEW_ALREADY_CREATED(HttpStatus.BAD_REQUEST, "이미 작성된 리뷰가 있습니다.")
    ;


    private final HttpStatus status;
    private final String message;
}
