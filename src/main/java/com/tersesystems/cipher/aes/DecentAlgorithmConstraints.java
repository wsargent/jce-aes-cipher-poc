package com.tersesystems.cipher.aes;

import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.AlgorithmConstraints;
import java.security.AlgorithmParameters;
import java.security.CryptoPrimitive;
import java.security.Key;
import java.security.spec.InvalidParameterSpecException;
import java.util.Set;

/**
 * Checks to see that the settings and keysizes are minimally decent.
 */
public class DecentAlgorithmConstraints implements AlgorithmConstraints {

    private final int minimumNonceLength = 12;

    public DecentAlgorithmConstraints() {
    }

    public boolean permits(Set<CryptoPrimitive> primitives, String algorithm,
                           AlgorithmParameters parameters) {
        return permits(primitives, algorithm, null, parameters);
    }

    public boolean permits(Set<CryptoPrimitive> primitives, Key key) {
        return permits(primitives, null, key, null);
    }

    public boolean permits(Set<CryptoPrimitive> primitives, String algorithm,
                           Key key, AlgorithmParameters parameters) {
        if (algorithm == null) {
            algorithm = key.getAlgorithm();
        }

        if (! algorithm.contains("AES")) {
            return false;
        }

        if (! primitives.contains(CryptoPrimitive.BLOCK_CIPHER)) {
            return false;
        }

        try {
            final GCMParameterSpec parameterSpec = parameters.getParameterSpec(GCMParameterSpec.class);

            // Checks that the IV length is minimal (and can also check that IV/key does not repeat)
            final byte[] iv = parameterSpec.getIV();
            if (iv.length < minimumNonceLength) {
                return false;
            }
            // authentication tag length.
            // parameterSpec.getTLen();

        } catch (InvalidParameterSpecException e) {
            return false;
        }

        if (key != null && key instanceof SecretKey) {
            SecretKey secretKey = (SecretKey) key;
            int size = secretKey.getEncoded().length;
            if (size < 32) {
                return false;
            }
        }

        return true;
    }
}
