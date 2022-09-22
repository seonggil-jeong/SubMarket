package com.submarket.userservice.exception;

import com.submarket.userservice.exception.result.ItemExceptionResult;
import com.submarket.userservice.exception.result.SubExceptionResult;
import com.submarket.userservice.exception.result.UserExceptionResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class UserServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(final UserException exception) {
        log.warn("UserException occur : ", exception);
        return this.makeErrorResponseEntity(exception.getExceptionResult());

    }

    @ExceptionHandler(SubException.class)
    public ResponseEntity<ErrorResponse> handleSubException(final SubException exception) {
        log.warn("SubException occur : ", exception);
        return this.makeErrorResponseEntity(exception.getExceptionResult());

    }

    @ExceptionHandler(ItemException.class)
    public ResponseEntity<ErrorResponse> handleItemException(final ItemException exception) {
        log.warn("ItemException occur : ", exception);
        return this.makeErrorResponseEntity(exception.getExceptionResult());

    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final UserExceptionResult exceptionResult) {
        return ResponseEntity.status(exceptionResult.getStatus())
                .body(new ErrorResponse(exceptionResult.name(), exceptionResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final SubExceptionResult exceptionResult) {
        return ResponseEntity.status(exceptionResult.getStatus())
                .body(new ErrorResponse(exceptionResult.name(), exceptionResult.getMessage()));
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final ItemExceptionResult exceptionResult) {
        return ResponseEntity.status(exceptionResult.getStatus())
                .body(new ErrorResponse(exceptionResult.name(), exceptionResult.getMessage()));
    }
}
