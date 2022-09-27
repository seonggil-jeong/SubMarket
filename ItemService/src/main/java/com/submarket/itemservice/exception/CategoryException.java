package com.submarket.itemservice.exception;

import com.submarket.itemservice.exception.result.CategoryExceptionResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CategoryException extends RuntimeException {


    private final CategoryExceptionResult exceptionResult;
}
