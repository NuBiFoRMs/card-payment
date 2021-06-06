package com.nubiform.payment.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nubiform.payment.config.PaymentType;
import com.nubiform.payment.vo.payload.PayloadField;
import com.nubiform.payment.vo.payload.PayloadFormatter;
import com.nubiform.payment.vo.payload.PayloadSerializable;
import com.nubiform.payment.vo.payload.PayloadSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonSerialize(using = PayloadSerializer.class)
public class PaymentPayload implements PayloadSerializable {

    @PayloadField(order = 0, length = 10)
    private PaymentType type;

    @PayloadField(order = 10, length = 20)
    private String id;

    @PayloadField(order = 30, length = 20)
    private String card;

    @PayloadField(formatter = PayloadFormatter.NUMBER_0, order = 40, length = 2)
    private Integer installment;

    @PayloadField(formatter = PayloadFormatter.NUMBER_L, order = 42, length = 4)
    private String expiration;

    @PayloadField(formatter = PayloadFormatter.NUMBER_L, order = 46, length = 3)
    private String cvc;

    @PayloadField(formatter = PayloadFormatter.NUMBER, order = 49, length = 10)
    private Long amount;

    @PayloadField(formatter = PayloadFormatter.NUMBER_0, order = 59, length = 10)
    private Long vat;

    @PayloadField(order = 69, length = 20)
    private String originId;

    @PayloadField(order = 89, length = 300)
    private String encryptedCard;

    @PayloadField(order = 389, length = 47)
    private String extraField;
}
