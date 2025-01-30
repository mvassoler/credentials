package br.com.mvassoler.credentials.core.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {

    private static final String ENCRYPTION_ALGORITHM = "AES"; // Algoritmo AES
    private static final String CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"; // Modo GCM
    private static final int GCM_TAG_LENGTH = 128; // Comprimento do bloco GCM
    private static final int IV_LENGTH = 12; // Tamanho do IV
    private static final int KEY_SIZE = 256; // Tamanho da chave (256 bits)

    // Gerar uma chave segura (a chave deve ser armazenada em um local seguro fora do código)
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        return keyGenerator.generateKey();
    }

    // Criptografar
    public static String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        byte[] iv = new byte[IV_LENGTH];
        new java.security.SecureRandom().nextBytes(iv); // Gerando IV seguro
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] encryptedData = cipher.doFinal(data.getBytes());
        byte[] encryptedDataWithIv = new byte[iv.length + encryptedData.length];

        System.arraycopy(iv, 0, encryptedDataWithIv, 0, iv.length); // Anexando IV
        System.arraycopy(encryptedData, 0, encryptedDataWithIv, iv.length, encryptedData.length);

        return Base64.getEncoder().encodeToString(encryptedDataWithIv);
    }

    // Descriptografar
    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] iv = new byte[IV_LENGTH];
        byte[] encryptedContent = new byte[decodedData.length - IV_LENGTH];

        System.arraycopy(decodedData, 0, iv, 0, iv.length);
        System.arraycopy(decodedData, iv.length, encryptedContent, 0, encryptedContent.length);

        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] decryptedData = cipher.doFinal(encryptedContent);
        return new String(decryptedData);
    }

    public static SecretKey getSecretKey(String chaveBase64) {
        return new SecretKeySpec(Base64.getDecoder().decode(chaveBase64), ENCRYPTION_ALGORITHM);
    }
}