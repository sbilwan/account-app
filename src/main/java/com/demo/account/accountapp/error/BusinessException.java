package com.demo.account.accountapp.error;

public class BusinessException extends RuntimeException {

    public BusinessException(final String message) {
        super(message);
    }
}
