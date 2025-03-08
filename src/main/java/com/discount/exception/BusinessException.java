package com.discount.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String message;
    private final int status;

    public BusinessException(String message, int status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
