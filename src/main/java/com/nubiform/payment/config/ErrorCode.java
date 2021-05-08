package com.nubiform.payment.config;

import lombok.Getter;

@Getter
public enum ErrorCode {

    OperationNotSupported(1000, "Operation not supported"),
    NoDataFound(1001, "No data found"),
    NotEnoughAmountOrVat(1002, "Not enough amount or vat"),
    PaymentIsProcessing(1003, "Payment is processing"),
    UnknownError(9999, "Unknown error");

    private int code;
    private String description;

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
