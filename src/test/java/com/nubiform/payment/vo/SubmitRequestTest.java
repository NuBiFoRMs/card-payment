package com.nubiform.payment.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubmitRequestTest {

    @Test
    public void testVat1() {
        SubmitRequest request = new SubmitRequest();
        request.setAmount(1000L);
        assertEquals(91, request.getVat());
    }

    @Test
    public void testVat2() {
        SubmitRequest request = new SubmitRequest();
        request.setAmount(995L);
        assertEquals(90, request.getVat());
    }

    @Test
    public void testVat3() {
        SubmitRequest request = new SubmitRequest();
        request.setAmount(1000L);
        request.setVat(1000L);
        assertEquals(1000, request.getVat());
    }
}