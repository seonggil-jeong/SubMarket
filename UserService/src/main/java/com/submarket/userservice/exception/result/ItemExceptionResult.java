package com.submarket.userservice.exception.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ItemExceptionResult {
    NOT_MATCHED_ITEM_SEQ(HttpStatus.BAD_REQUEST, "잘못된 상품 번호 입니다"),
    ;
    private final HttpStatus status;
    private final String message;
}
