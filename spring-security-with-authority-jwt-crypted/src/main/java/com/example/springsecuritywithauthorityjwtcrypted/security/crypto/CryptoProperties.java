package com.example.springsecuritywithauthorityjwtcrypted.security.crypto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties("crypto")
public class CryptoProperties {
  private String secretKey;
  private String secretKeyAlgorithm;
  private String transformation;
  private int gcmTagLength;
  private int gcmIvLength;
}
