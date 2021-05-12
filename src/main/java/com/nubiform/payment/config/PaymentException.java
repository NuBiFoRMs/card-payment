package com.nubiform.payment.config;

import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException {

    private final int code;

    public PaymentException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.code = errorCode.getCode();
    }
}
