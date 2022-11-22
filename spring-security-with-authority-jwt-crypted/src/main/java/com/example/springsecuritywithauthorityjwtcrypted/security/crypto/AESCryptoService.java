package com.example.springsecuritywithauthorityjwtcrypted.security.crypto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public final class AESCryptoService implements CryptoService {

  private final CryptoProperties cryptoProperties;

  @Override
  public String encrypt(final String rawData)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
          IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
          ShortBufferException {
    final var initVectorLength = cryptoProperties.getGcmIvLength();
    final var initVector = new byte[initVectorLength];
    final var cipher = buildCipher(Cipher.ENCRYPT_MODE, initVector);
    final var rawDataAsBytes = rawData.getBytes(StandardCharsets.UTF_8);
    final var dataLength = rawDataAsBytes.length;
    final var cipherTextLength = initVectorLength + cipher.getOutputSize(dataLength);
    final var ciphertext = new byte[cipherTextLength];
    System.arraycopy(initVector, 0, ciphertext, 0, initVectorLength);
    cipher.doFinal(rawDataAsBytes, 0, dataLength, ciphertext, initVectorLength);
    return encode(ciphertext);
  }

  @Override
  public String decrypt(final String cipherText)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
          BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
    final var encryptedData = decode(cipherText);
    final var initVectorLength = cryptoProperties.getGcmIvLength();
    final var initVector = Arrays.copyOfRange(encryptedData, 0, initVectorLength);
    final var cipher = buildCipher(Cipher.DECRYPT_MODE, initVector);
    final var inputLength = encryptedData.length - initVectorLength;
    final var decryptedData = cipher.doFinal(encryptedData, initVectorLength, inputLength);
    return new String(decryptedData, StandardCharsets.UTF_8);
  }

  private Cipher buildCipher(final int mode, final byte[] initVector)
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
          InvalidAlgorithmParameterException {
    final var cipher = Cipher.getInstance(cryptoProperties.getTransformation());
    final var tagLength = cryptoProperties.getGcmTagLength() * Byte.SIZE;
    final var algorithmSpec = new GCMParameterSpec(tagLength, initVector);
    cipher.init(mode, getSecretKey(), algorithmSpec);
    return cipher;
  }

  private Key getSecretKey() {
    final var secretKeyBytes = cryptoProperties.getSecretKey().getBytes();
    return new SecretKeySpec(secretKeyBytes, cryptoProperties.getSecretKeyAlgorithm());
  }

  private String encode(final byte[] data) {
    return Base64.getEncoder().encodeToString(data);
  }

  @NonNull
  private byte[] decode(final String data) {
    return Base64.getDecoder().decode(data);
  }
}
