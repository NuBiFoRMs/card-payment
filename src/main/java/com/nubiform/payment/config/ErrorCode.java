package com.nubiform.payment.config;

import lombok.Getter;

@Getter
public enum ErrorCode {

    OperationNotSupported(9999, "Operation not supported"),
    NoDataFound(1000, "No data found"),
    NotEnoughAmountOrVat(1001, "Not enough amount or vat");

    private int code;
    private String description;

    ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
