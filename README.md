# AES Proof of Concept

This is a "not horribly wrong" proof of concept of AES-GCM in Java, using JDK 1.8.  

I wrote this because there's a horrible lack of examples of AES-GCM floating around.

## Warning

I am not a professional cryptographer, and this is a proof of concept.

Please look at Kalium / NaCl / Keyczar if you're looking for symmetric encryption.

## Not Horribly Wrong?

This implementation uses AES-GCM with a programming model where:

* It is not the user's responsibility to provide a random nonce.
* The nonce is generated using `SecureRandom.getInstanceStrong`.
* The secret key can have an expiration date to minimize possible key/IV reuse.
* All access to the secret key goes through validation first.
* Validation includes an `AlgorithmConstraints` interface to check for weak key sizes etc.
* The API has actual type safety, rather than being passed around as byte arrays.

## Requirements

You will need the Java Cryptography Extension (JCE) Unlimited Strength policy files installed for AES-256.

## Example please?

```java
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
```

## Bugs?

Please file an issue or send email to [will.sargent+github@gmail.com](mailto:will.sargent+github@gmail.com).
