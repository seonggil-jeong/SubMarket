package com.submarket.userservice.exception;

import com.submarket.userservice.exception.result.UserExceptionResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserException extends RuntimeException {

    private final UserExceptionResult exceptionResult;
}
