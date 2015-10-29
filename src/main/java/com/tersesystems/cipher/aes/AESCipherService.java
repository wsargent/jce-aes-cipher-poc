package com.tersesystems.cipher.aes;

import com.tersesystems.cipher.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

/**
 * A type safe front end to an authenticated encryption service.
 */
public class AESCipherService {

    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int GCM_NONCE_LENGTH = 12; // in bytes
    private static final int GCM_TAG_LENGTH = 16; // in bytes

    private final CipherContext context;

    public AESCipherService(CipherContext context) throws NoSuchAlgorithmException {
        this.context = context;
    }

    /**
     * Encrypts some plaintext data and passes in some data to be authenticated later.
     *
     * @param input plaintext
     * @param aad auth data
     * @return encrypted data
     * @throws GeneralSecurityException
     */
    public EncryptedData encrypt(PlainText input, AuthenticationData aad) throws GeneralSecurityException {
        if (input == null) {
            throw new IllegalArgumentException("Null input!");
        }
        // You can encrypt about 68 GB (2**39 − 256 in bits) for a single nonce, but JCE
        // buffers plaintext during decryption, so you really wouldn't want to.
        final Nonce nonce = context.generateNonce(GCM_NONCE_LENGTH);
        final Cipher cipher = initEncryptCipher(nonce, aad);

        final CipherText cipherText = new CipherText(cipher.doFinal(input.getBytes()));
        return new EncryptedData(nonce, cipherText);
    }

    /**
     * Decrypts encrypted data to plaintext.
     *
     * @param data encrypted data.
     * @param aad data to authenticate.
     * @return the plain text.
     * @throws GeneralSecurityException
     */
    public PlainText decrypt(EncryptedData data, AuthenticationData aad) throws GeneralSecurityException {
        final Cipher cipher = initDecryptCipher(data, aad);
        final byte[] plainText = cipher.doFinal(data.getCipherText().getBytes());
        return new PlainText(plainText);
    }

    private Cipher initEncryptCipher(Nonce nonce, AuthenticationData aad) throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(AES_GCM);
        final AlgorithmParameters params = initAlgorithmParameters(nonce);
        final SecretKey secretKey = context.getSecretKey(cipher, params);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, params);
        cipher.updateAAD(aad.getData());
        return cipher;
    }

    private Cipher initDecryptCipher(EncryptedData data, AuthenticationData aad) throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(AES_GCM);
        final AlgorithmParameters params = initAlgorithmParameters(data.getNonce());
        final SecretKey secretKey = context.getSecretKey(cipher, params);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
        cipher.updateAAD(aad.getData());
        return cipher;
    }

    private AlgorithmParameters initAlgorithmParameters(Nonce nonce) throws GeneralSecurityException {
        final GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce.getBytes());
        final AlgorithmParameters params = AlgorithmParameters.getInstance("GCM");
        params.init(spec);
        return params;
    }
}

