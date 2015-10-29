package com.tersesystems.cipher;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 *
 *
 */
public class NonceGenerator {

    // An IV must be random and unpredictable.
    // A nonce doesn't have to be -- it could be a counter, for example.
    // Neither IVs nor Nonces should be reused.
    //
    // AES-GCM doesn't require a random nonce, but... never reusing a nonce means
    // storing some state somewhere, potentially involving IO, blocking, etc.
    // If you use a random number as a nonce, you have a very small chance of reuse.
    // So... you can either expire the key well before the chance gets too big,
    // or you can keep a rough count of how many times the nonce has been used
    // with a certain key.
    //
    // The general advice is that:
    // most significant bits should be random...
    // last 6 to 8 bytes should be sequential.
    //
    // The problem is that GCM only has 12 bytes to play with.  This is where
    // NaCl looks good, because the nonce space is that much larger.
    // So, let's go full random.

    private final SecureRandom random;

    public NonceGenerator() throws NoSuchAlgorithmException {
        // You care about high-quality continuous-availability randomness for the nonces,
        // where biases can be devastating to security, so get the strongest possible
        // SecureRandom.
        this.random = SecureRandom.getInstanceStrong();
        random.nextBytes(new byte[55]); // force a self-seeding immediately if SHA1PRNG
    }

    public Nonce generateNonce(int length) {
        final byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return new Nonce(bytes);
    }
}
