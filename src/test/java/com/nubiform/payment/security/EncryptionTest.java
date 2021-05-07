package com.nubiform.payment.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EncryptionTest {

    public static final String PLAIN_TEXT = "plain text";

    @Autowired
    Encryption encryption;

    @Test
    public void test() throws Exception {
        assertEquals(PLAIN_TEXT, encryption.decrypt(encryption.encrypt(PLAIN_TEXT)));
    }
}