package com.example.springsecuritywithauthorityjwtcrypted.security.token;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtConfiguration {
  private final SecretKey secretKey;
  private final MacAlgorithm macAlgorithm;

  public JwtConfiguration(final JwtProperties jwtProperties) {
    final var secret = jwtProperties.getSecretKey().getBytes();
    secretKey = new SecretKeySpec(secret, jwtProperties.getAlgorithm());
    macAlgorithm = MacAlgorithm.valueOf(jwtProperties.getAlgorithm());
  }

  @Bean
  JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(macAlgorithm).build();
  }

  @Bean
  JwtEncoder jwtEncoder() {
    final var jwks = new ImmutableSecret<>(secretKey);
    return new NimbusJwtEncoder(jwks);
  }
}
