package com.nubiform.payment.config;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NoDataFound(1000, "No data found"),
    NotEnoughAmountOrVat(1001, "Not enough amount or vat"),
    PaymentIsProcessing(1002, "Payment is processing"),
    PaymentIsAlreadyCanceled(1003, "Payment is already canceled"),

    UnknownError(9999, "Unknown error");

    private int code;
    private String description;

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
