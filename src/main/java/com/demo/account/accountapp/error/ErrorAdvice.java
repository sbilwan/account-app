package com.demo.account.accountapp.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class ErrorAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorAdvice.class);

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity handleDbOperationException(BusinessException exception) {
        LOGGER.error(" DB operation error encountered " + exception.getMessage());
        return createErrorResponse(new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()));
    }

    @ExceptionHandler(SQLException.class)
    @ResponseBody
    public ResponseEntity handleSQLException(SQLException exception) {
        LOGGER.error(" Runtime SQLException exception encountered " + exception.getMessage());
        return createErrorResponse(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while processing request"));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity handleAnException(RuntimeException exception) {
        LOGGER.error(" Runtime SQLException exception encountered " + exception.getMessage());
        return createErrorResponse(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server error"));
    }

    private ResponseEntity createErrorResponse(ApiErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getHttpStatusCode()));
    }

}
