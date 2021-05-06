package com.nubiform.payment.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AES256Test {

    public static final String PLAIN_TEXT = "plain text";

    @Test
    public void test() throws Exception {
        AES256 aes256 = new AES256();
        assertEquals(PLAIN_TEXT, aes256.decrypt(aes256.encrypt(PLAIN_TEXT)));
    }

}