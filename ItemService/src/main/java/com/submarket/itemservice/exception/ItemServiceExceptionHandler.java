package com.submarket.itemservice.exception;

import com.submarket.itemservice.exception.ErrorResponse;
import com.submarket.itemservice.exception.ItemException;
import com.submarket.itemservice.exception.ItemReviewException;
import com.submarket.itemservice.exception.result.CategoryExceptionResult;
import com.submarket.itemservice.exception.result.ItemExceptionResult;
import com.submarket.itemservice.exception.result.ItemReviewExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ItemServiceExceptionHandler extends ResponseEntityExceptionHandler {


    // Catch ItemException
    @ExceptionHandler({ItemException.class})
    public ResponseEntity<ErrorResponse> handleItemException(final ItemException exception) {
        log.warn("ItemException occur : ", exception);
        return this.makeErrorResponseEntity(exception.getExceptionResult());
    }


    // Catch ItemReviewException
    @ExceptionHandler(ItemReviewException.class)
    public ResponseEntity<ErrorResponse> handleItemReviewException(final ItemReviewException exception) {
        log.warn("ItemReviewException occur : ", exception);
        return this.makeErrorResponseEntity(exception.getExceptionResult());
    }


    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<ErrorResponse> handleCategoryException(final CategoryException exception) {
        log.warn("CategoryException occur : ", exception);
        return this.makeErrorResponseEntity(exception.getExceptionResult());

    }


    /**
     * Exception 정보에서 ResponseEntity(ErrorResponse 생성)
     * -> 각 Exception 에 맞에 Param 변경
     * @param exceptionResult
     * @return
     */

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final CategoryExceptionResult exceptionResult) {
        return ResponseEntity.status(exceptionResult.getStatus())
                .body(new ErrorResponse(exceptionResult.name(), exceptionResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final ItemReviewExceptionResult exceptionResult) {
        return ResponseEntity.status(exceptionResult.getStatus())
                .body(new ErrorResponse(exceptionResult.name(), exceptionResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final ItemExceptionResult exceptionResult) {
        return ResponseEntity.status(exceptionResult.getStatus())
                .body(new ErrorResponse(exceptionResult.name(), exceptionResult.getMessage()));
    }
}
