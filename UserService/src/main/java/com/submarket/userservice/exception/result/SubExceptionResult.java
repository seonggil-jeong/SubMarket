package com.submarket.userservice.exception.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SubExceptionResult {
    CAN_NOT_FOUND_SUB_INFO(HttpStatus.NOT_FOUND, "일치하는 구독 정보를 찾을 수 없습니다"),
    ;

    private final HttpStatus status;
    private final String message;
}
