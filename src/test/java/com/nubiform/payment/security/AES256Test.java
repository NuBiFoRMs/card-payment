package com.nubiform.payment.security;

import com.nubiform.payment.api.vo.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AES256Test {

    public static final String PLAIN_TEXT = "plain text";

    @Test
    public void test() throws Exception {
        AES256 aes256 = new AES256();
        assertEquals(PLAIN_TEXT, aes256.decrypt(aes256.encrypt(PLAIN_TEXT)));
    }

    @Test
    public void testCard10() throws Exception {
        Card card = new Card();
        card.setCard("1234567890");
        card.setExpiration("1234");
        card.setCvc("123");

        AES256 aes256 = new AES256();
        String encrypt = aes256.encrypt(card.toData());
        System.out.println(encrypt);
        Card newCard = new Card(aes256.decrypt(encrypt));

        assertEquals(card.getCard(), newCard.getCard());
        assertEquals(card.getExpiration(), newCard.getExpiration());
        assertEquals(card.getCvc(), newCard.getCvc());
    }

    @Test
    public void testCard16() throws Exception {
        Card card = new Card();
        card.setCard("1234567890123456");
        card.setExpiration("1234");
        card.setCvc("123");

        AES256 aes256 = new AES256();
        String encrypt = aes256.encrypt(card.toData());
        System.out.println(encrypt);
        Card newCard = new Card(aes256.decrypt(encrypt));

        assertEquals(card.getCard(), newCard.getCard());
        assertEquals(card.getExpiration(), newCard.getExpiration());
        assertEquals(card.getCvc(), newCard.getCvc());
    }
}