package com.submarket.userservice.exception.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionResult {
    USER_NOT_FOUNT(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다"),
    CAN_NOT_CREATE_KAFKA_TOPIC(HttpStatus.INTERNAL_SERVER_ERROR, "Exception in Kafka"),
    ;


    private final HttpStatus status;
    private final String message;
}
