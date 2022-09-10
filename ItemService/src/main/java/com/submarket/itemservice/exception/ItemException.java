package com.submarket.itemservice.exception;

import com.submarket.itemservice.exception.result.ItemExceptionResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemException extends RuntimeException {


    private final ItemExceptionResult exceptionResult;
}
