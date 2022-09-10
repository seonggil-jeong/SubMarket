package com.submarket.itemservice.exception.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ItemExceptionResult {
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다"),
    ;


    private final HttpStatus status;
    private final String message;
    }
