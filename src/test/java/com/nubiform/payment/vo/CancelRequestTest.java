package com.nubiform.payment.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CancelRequestTest {

    @Test
    public void testVat1() {
        CancelRequest request = new CancelRequest();
        request.setAmount(1000L);
        assertEquals(91, request.getVat());
    }

    @Test
    public void testVat2() {
        CancelRequest request = new CancelRequest();
        request.setAmount(995L);
        assertEquals(90, request.getVat());
    }

    @Test
    public void testVat3() {
        CancelRequest request = new CancelRequest();
        request.setAmount(1000L);
        request.setVat(1000L);
        assertEquals(1000, request.getVat());
    }
}