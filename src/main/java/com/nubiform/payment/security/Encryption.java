package com.nubiform.payment.security;

public interface Encryption {
    
    String encrypt(String text) throws Exception;

    String decrypt(String cipherText) throws Exception;
}
