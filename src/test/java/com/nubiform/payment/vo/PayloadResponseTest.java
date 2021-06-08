package com.nubiform.payment.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.config.PaymentType;
import com.nubiform.payment.vo.id.PaymentId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PayloadResponseTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void test1() throws JsonProcessingException {
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

        PaymentResponse<PaymentPayload> payloadResponse = new PaymentResponse<>();
        payloadResponse.setId(PaymentId.of(10L));
        payloadResponse.setData(paymentPayload);

        String response = objectMapper.writeValueAsString(payloadResponse);

        assertEquals("{\"id\":\"N0000000000000000010\",\"data\":\" 446PAYMENT   N00000000000000000012                   034   5           60000000007N00000000000000000089                                                                                                                                                                                                                                                                                                           10                                             \"}", response);
    }

    @Test
    public void test2() throws JsonProcessingException {
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

        PaymentResponse<PaymentPayload> payloadResponse = new PaymentResponse<>();
        payloadResponse.setId(PaymentId.of(10L));
        payloadResponse.setData(paymentPayload);

        String response = objectMapper.writeValueAsString(payloadResponse);

        assertEquals("{\"id\":\"N0000000000000000010\",\"data\":\" 446PAYMENT   N00000000000000000012                   034   5           60000000007                    9                                                                                                                                                                                                                                                                                                           10                                             \"}", response);
    }
}