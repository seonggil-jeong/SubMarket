package com.submarket.userservice.exception.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionResult {
    USER_NOT_FOUNT(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다"),
    CAN_NOT_CREATE_KAFKA_TOPIC(HttpStatus.INTERNAL_SERVER_ERROR, "Exception in Kafka"),
    USER_PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "사용자 비밀번호가 일치하지 않습니다"),
    ;


    private final HttpStatus status;
    private final String message;
}
