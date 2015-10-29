package com.tersesystems.cipher;

/**
 * Contains encrypted data and nonce.
 *
 * Returning EncryptedData means that users are not tempted to reuse a nonce, and it is
 * neatly returned with the data.
 */
public class EncryptedData {
    private final Nonce nonce;
    private final CipherText cipherText;

    public EncryptedData(Nonce nonce, CipherText cipherText) {
        this.nonce = nonce;
        this.cipherText = cipherText;
    }

    public CipherText getCipherText() {
        return cipherText;
    }

    public Nonce getNonce() {
        return nonce;
    }
}
