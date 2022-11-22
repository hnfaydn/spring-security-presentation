package com.example.springsecuritywithauthorityjwtcrypted.security.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface CryptoService {
  String encrypt(String plainText)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
          IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
          ShortBufferException;

  String decrypt(String cipherText)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
          BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException;
}
