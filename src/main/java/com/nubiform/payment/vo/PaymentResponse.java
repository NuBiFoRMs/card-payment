package com.nubiform.payment.vo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class PaymentResponse {

    public static final String PRE_FIX = "N";
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
        return PRE_FIX + StringUtils.leftPad(String.valueOf(id), 19, "0");
    }

    @JsonGetter("originId")
    public String getOriginId() {
        return PRE_FIX + StringUtils.leftPad(String.valueOf(originId), 19, "0");
    }

    @JsonGetter("card")
    public String getCard() {
        return this.card.replaceAll(MASK_REGEX, MASK);
    }

    @JsonIgnore
    public Long getLongId() {
        return this.id;
    }
}
