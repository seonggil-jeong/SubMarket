package com.submarket.userservice.exception;

import com.submarket.userservice.exception.result.ItemExceptionResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemException extends RuntimeException{
    private final ItemExceptionResult exceptionResult;
}
