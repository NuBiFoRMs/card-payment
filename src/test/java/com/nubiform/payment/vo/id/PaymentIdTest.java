package com.nubiform.payment.vo.id;

import com.nubiform.payment.config.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentIdTest {

    @Test
    public void valueTest() {
        PaymentId paymentId = PaymentId.of(50L);
        assertEquals(50L, paymentId.value());
    }

    @Test
    public void stringValueTest() {
        PaymentId paymentId = PaymentId.of(50L);
        assertEquals("N0000000000000000050", paymentId.toString());
    }

    @Test
    public void convertToStringTest() {
        PaymentId paymentId = PaymentId.of("N0000000000000000050");
        assertEquals(50L, paymentId.value());
    }

    @Test
    public void exceptionTest() {
        assertThrows(ValidationException.class, () -> PaymentId.of("test"));
    }

    @Test
    public void exceptionMinusTest() {
        assertThrows(ValidationException.class, () -> PaymentId.of(-1));
    }

    @Test
    public void exceptionConvertTest() {
        assertThrows(ValidationException.class, () -> PaymentId.convert(-1));
    }

    @Test
    public void nullPointerExceptionTest() {
        assertThrows(NullPointerException.class, () -> PaymentId.of(null));
    }
}