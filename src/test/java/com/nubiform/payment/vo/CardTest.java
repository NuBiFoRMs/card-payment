package com.nubiform.payment.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardTest {

    @Test
    public void testCard() {
        Card card = new Card("1234567890123456|1234|123");

        assertEquals("1234567890123456", card.getCard());
        assertEquals("1234", card.getExpiration());
        assertEquals("123", card.getCvc());
    }

    @Test
    public void testToData() {
        Card card = new Card();
        card.setCard("1234567890123456");
        card.setExpiration("1234");
        card.setCvc("123");

        assertEquals("1234567890123456|1234|123", card.toData());
    }
}