package com.nubiform.payment.vo;

import lombok.Data;

@Data
public class PaymentResponse {

    private String id;

    private String type;

    private String card;

    private String expiration;

    private String cvc;

    private Long amount;

    private Long vat;
}
