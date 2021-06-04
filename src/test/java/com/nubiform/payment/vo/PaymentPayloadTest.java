package com.nubiform.payment.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
                .build();

        System.out.println(">" + paymentPayload.serialize() + "<");

        assertTrue(true);
    }

}