package com.nubiform.payment.vo.payload;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PayloadTest {

    @Test
    public void test() {
        System.out.println(PayloadSerializer.serializer(new TestPayload()));

        assertTrue(true);
    }


    class TestPayload {

        @PayloadField(formatter = PayloadFormatter.STRING, start = 0, length = 5)
        String field1 = "test";

        @PayloadField(formatter = PayloadFormatter.STRING, start = 5, length = 5)
        String field2 = "test";
    }
}