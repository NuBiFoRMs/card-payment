package com.nubiform.payment.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.config.PaymentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PayloadResponseTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void test() throws JsonProcessingException {
        PaymentPayload paymentPayload = PaymentPayload.builder()
                .type(PaymentType.PAYMENT)
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

        PaymentResponse<PaymentPayload> payloadResponse = new PaymentResponse<>();
        payloadResponse.setId(10L);
        payloadResponse.setData(paymentPayload);

        String response = objectMapper.writeValueAsString(payloadResponse);

        assertEquals("{\"data\":\" 446PAYMENT   1                   2                   034   5           600000000078                   9                                                                                                                                                                                                                                                                                                           10                                             \",\"id\":\"N0000000000000000010\"}", response);
    }
}