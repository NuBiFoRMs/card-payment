package com.nubiform.payment.vo;

import com.nubiform.payment.config.PaymentType;
import com.nubiform.payment.vo.id.PaymentId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentPayloadTest {

    @Test
    public void test1() {
        PaymentPayload paymentPayload = PaymentPayload.builder()
                .type(PaymentType.PAYMENT)
                .id(PaymentId.of(1L))
                .card("2")
                .installment(3)
                .expiration("4")
                .cvc("5")
                .amount(6L)
                .vat(7L)
                .originId(PaymentId.of(8L))
                .encryptedCard("9")
                .extraField("10")
                .build();

        assertEquals(" 446PAYMENT   N00000000000000000012                   034   5           60000000007N00000000000000000089                                                                                                                                                                                                                                                                                                           10                                             ",
                paymentPayload.serialize());
    }

    @Test
    public void test2() {
        PaymentPayload paymentPayload = PaymentPayload.builder()
                .type(PaymentType.PAYMENT)
                .id(PaymentId.of(1L))
                .card("2")
                .installment(3)
                .expiration("4")
                .cvc("5")
                .amount(6L)
                .vat(7L)
                .encryptedCard("9")
                .extraField("10")
                .build();

        assertEquals(" 446PAYMENT   N00000000000000000012                   034   5           60000000007                    9                                                                                                                                                                                                                                                                                                           10                                             ",
                paymentPayload.serialize());
    }
}