package com.nubiform.payment.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentPayloadTest {

    @Test
    public void test() {
        PaymentPayload paymentPayload = PaymentPayload.builder()
                .type("0")
                .id("1")
                .card("2")
                .installment(3)
                .expiration("4")
                .cvc("5")
                .amount(6L)
                .vat(7L)
                .originId("8")
                .encryptedCard("9")
                .extraField("10")
                .build();

        assertEquals(" 4460         1                   2                   034   5           600000000078                   9                                                                                                                                                                                                                                                                                                           10                                             ",
                paymentPayload.serialize());
    }

}