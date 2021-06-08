package com.nubiform.payment.vo.id;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nubiform.payment.config.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PaymentIdSerializerAndDeserializeTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void serializeTest() throws JsonProcessingException {
        PaymentId paymentId = PaymentId.of(50L);
        String json = objectMapper.writeValueAsString(paymentId);
        assertEquals("\"N0000000000000000050\"", json);
    }

    @Test
    public void deserializeTest() throws JsonProcessingException {
        PaymentId paymentId = objectMapper.readValue("\"N0000000000000000050\"", PaymentId.class);
        assertEquals(50L, paymentId.value());
    }

    @Test
    public void deserializeExceptionTest() {
        assertThrows(ValidationException.class, () -> objectMapper.readValue("\"test\"", PaymentId.class));
    }
}