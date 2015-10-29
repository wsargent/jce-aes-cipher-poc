package com.tersesystems.cipher;

/**
 *
 */
public class Nonce {

    private final byte[] bytes;

    public Nonce(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
