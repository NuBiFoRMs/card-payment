package com.nubiform.payment.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;

@Data
public class PaymentResponse {

    public static final String MASK_REGEX = "(?<=.{6}).(?=.{3})";
    public static final String MASK = "*";

    private Long id;

    private String type;

    private String card;

    private String expiration;

    private String cvc;

    private Long amount;

    private Long vat;

    private Long originId;

    private Long totalAmount;

    private Long totalVat;

    private Long remainAmount;

    private Long remainVat;

    @JsonGetter("id")
    public String getId() {
        return Id.convert(this.id);
    }

    @JsonGetter("originId")
    public String getOriginId() {
        return Id.convert(this.originId);
    }

    @JsonGetter("card")
    public String getCard() {
        return this.card.replaceAll(MASK_REGEX, MASK);
    }
}
