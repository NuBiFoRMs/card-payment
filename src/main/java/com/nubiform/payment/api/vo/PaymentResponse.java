package com.nubiform.payment.api.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PaymentResponse {

    private String id;

    private String type;

    private String card;

    private String expiration;

    private String cvc;

    private Long amount;

    private Long vat;
}
