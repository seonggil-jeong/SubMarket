package com.submarket.itemservice.exception;

import com.submarket.itemservice.exception.result.ItemReviewExceptionResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ItemReviewException extends RuntimeException {


    private final ItemReviewExceptionResult exceptionResult;
}
