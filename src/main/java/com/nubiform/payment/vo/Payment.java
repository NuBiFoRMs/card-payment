package com.nubiform.payment.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.nubiform.payment.vo.id.PaymentId;
import lombok.Data;

@Data
public class Payment {

    public static final String MASK_REGEX = "(?<=.{6}).(?=.{3})";
    public static final String MASK = "*";

    private String type;

    private String card;

    private String expiration;

    private String cvc;

    private Long amount;

    private Long vat;

    private PaymentId originId;

    private Long totalAmount;

    private Long totalVat;

    private Long remainAmount;

    private Long remainVat;

    @JsonGetter("card")
    public String getCard() {
        return this.card.replaceAll(MASK_REGEX, MASK);
    }
}
