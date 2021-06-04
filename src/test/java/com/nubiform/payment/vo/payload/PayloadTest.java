package com.nubiform.payment.vo.payload;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PayloadTest {

    @Test
    public void payloadTest1() {
        String payload = Optional.ofNullable(new TestPayload1())
                .map(TestPayload1::serialize)
                .get();

        assertEquals("  25field2    field1    00030", payload);
    }

    private class TestPayload1 implements PayloadSerializable {

        @PayloadField(formatter = PayloadFormatter.STRING, order = 1, length = 10)
        String field1 = "field1";

        @PayloadField(formatter = PayloadFormatter.STRING, order = 0, length = 10)
        String field2 = "field2";

        @PayloadField(formatter = PayloadFormatter.NUMBER_0, order = 2, length = 5)
        int field3 = 30;
    }

    @Test
    public void payloadTest2() {
        String payload = Optional.ofNullable(new TestPayload2())
                .map(TestPayload2::serialize)
                .get();

        assertEquals("  25field1    field2    00030", payload);
    }

    private class TestPayload2 implements PayloadSerializable {

        @PayloadField(formatter = PayloadFormatter.STRING, order = 0, length = 10)
        String field2 = "field2";

        @PayloadField(formatter = PayloadFormatter.STRING, order = 0, length = 10)
        String field1 = "field1";

        @PayloadField(formatter = PayloadFormatter.NUMBER_0, order = 2, length = 5)
        int field3 = 30;
    }
}