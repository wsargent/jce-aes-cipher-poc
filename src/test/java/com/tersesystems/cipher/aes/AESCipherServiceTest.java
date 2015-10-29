package com.tersesystems.cipher.aes;

import com.tersesystems.cipher.*;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.AlgorithmConstraints;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class AESCipherServiceTest {

    @Test
    public void testCipher() throws Exception {
        final AESCipherService cipherService = generateCipherService();

        final PlainText input = new PlainText("Hello world!");
        final AuthenticationData aad = new AuthenticationData(("" + input.getBytes().length + "/UTF-8").getBytes());

        final EncryptedData data = cipherService.encrypt(input, aad);
        final PlainText plainText = cipherService.decrypt(data, aad);

        Assert.assertEquals(input, plainText);
    }

    private AESCipherService generateCipherService() throws Exception {
        final AESKeyGenerator keyGenerator = new AESKeyGenerator();
        final SecretKey secretKey = keyGenerator.generateKey();

        final NonceGenerator nonceGenerator = new NonceGenerator();
        final Clock clock = Clock.systemUTC();
        final Instant expirationDate = clock.instant().plus(Duration.ofDays(10));
        final AlgorithmConstraints constraints = new DecentAlgorithmConstraints();

        final CipherContext context = new CipherContext(secretKey, constraints, expirationDate, nonceGenerator, clock);
        return new AESCipherService(context);
    }
}
