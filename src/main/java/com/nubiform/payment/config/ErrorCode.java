package com.nubiform.payment.config;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NoDataFound(1000, "No data found"),
    NotEnoughAmountOrVat(1001, "Not enough amount or vat"),
    PaymentIsProcessing(1002, "Payment is processing"),
    PaymentIsAlreadyCanceled(1003, "Payment is already canceled"),
    VatIsNotGreaterThanAmount(1004, "Vat is not greater than amount"),

    UnknownError(9999, "Unknown error");

    private final int code;
    private final String description;

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
