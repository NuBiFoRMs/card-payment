package com.nubiform.payment.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IdTest {

    @Test
    void convert() {
        assertEquals("N0000000000000000001", Id.convert(1L));
        assertEquals("                    ", Id.convert(-1L));
        assertEquals("                    ", Id.convert((Long) null));

        assertEquals(1L, Id.convert("N0000000000000000001"));
        assertNull(Id.convert("                    "));
        assertNull(Id.convert((String) null));
    }
}