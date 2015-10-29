package com.tersesystems.cipher;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.AlgorithmConstraints;
import java.security.AlgorithmParameters;
import java.security.CryptoPrimitive;
import java.security.GeneralSecurityException;
import java.time.Clock;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class CipherContext {

    private final SecretKey secretKey;
    private final Instant expirationDate;
    private final Clock clock;
    private final NonceGenerator nonceGenerator;
    private final AlgorithmConstraints constraints;


    public CipherContext(SecretKey secretKey,
                         AlgorithmConstraints constraints,
                         Instant expirationDate,
                         NonceGenerator nonceGenerator,
                         Clock clock) throws GeneralSecurityException {
        this.secretKey = secretKey;
        this.expirationDate = expirationDate;
        this.nonceGenerator = nonceGenerator;
        this.clock = clock;
        this.constraints = constraints;
    }

    public Nonce generateNonce(int length) {
        // You can encrypt 2 ** 32 times with random nonces on the same key, so
        // you could keep a counter here.
        return nonceGenerator.generateNonce(length);
    }

    public SecretKey getSecretKey(Cipher cipher, AlgorithmParameters params) {
        final Set<CryptoPrimitive> primitives = new HashSet<CryptoPrimitive>();
        primitives.add(CryptoPrimitive.BLOCK_CIPHER);

        if (! constraints.permits(primitives, cipher.getAlgorithm(), secretKey, params)) {
            throw new IllegalStateException();
        }

        final boolean isExpired = expirationDate.isBefore(clock.instant());
        if (isExpired) {
            throw new IllegalStateException();
        }

        return secretKey;
    }
}
