package com.nubiform.payment.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AES256 implements Encryption {

    public static final String ALGORITHM = "AES";
    public static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    public static final String DEFAULT_KEY = "0123456789012345678901234567890123456789";

    private final String key;
    private final String iv;

    public AES256() {
        this.key = DEFAULT_KEY;
        this.iv = key.substring(0, 16);
    }

    public AES256(String key) {
        this.key = key;
        this.iv = key.substring(0, 16);
    }

    public String encrypt(String text) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), ALGORITHM);
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), ALGORITHM);
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
