package com.submarket.userservice.exception;

import com.submarket.userservice.exception.result.SubExceptionResult;
import com.submarket.userservice.exception.result.UserExceptionResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SubException extends RuntimeException {

    private final SubExceptionResult exceptionResult;
}
