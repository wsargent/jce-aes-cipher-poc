package com.tersesystems.cipher;

/**
 * AAD is optional clear text. Only the integrity of AAD is assured.
 * A typical use case for additional data is to store protocol-specific metadata about
 * the message, such as its length and encoding, sequence number etc.
 *
 * http://crypto.stackexchange.com/a/15701/10979
 */
public class AuthenticationData {

    private final byte[] data;

    public AuthenticationData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
