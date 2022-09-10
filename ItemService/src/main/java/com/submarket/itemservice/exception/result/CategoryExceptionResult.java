package com.submarket.itemservice.exception.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CategoryExceptionResult {
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Category 정보를 찾을 수 없습니다"),
    ;


    private final HttpStatus status;
    private final String message;
}
