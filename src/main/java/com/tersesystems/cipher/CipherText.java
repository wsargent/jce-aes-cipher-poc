package com.tersesystems.cipher;

/**
 *
 */
public class CipherText {
    private final byte[] bytes;

    public CipherText(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
