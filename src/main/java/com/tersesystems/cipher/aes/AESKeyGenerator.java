package com.tersesystems.cipher.aes;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Generates an AES key from random.
 */
public class AESKeyGenerator {
    // Note you must have "Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 8" installed
    // to generate 256 bit AES keys.
    public static final int AES_DEFAULT_KEY_SIZE = 256; // in bits

    public SecretKey generateKey() throws NoSuchAlgorithmException {
        final SecureRandom random = SecureRandom.getInstanceStrong();
        final KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(AES_DEFAULT_KEY_SIZE, random);
        final SecretKey key = keyGen.generateKey();
        return key;
    }

}
