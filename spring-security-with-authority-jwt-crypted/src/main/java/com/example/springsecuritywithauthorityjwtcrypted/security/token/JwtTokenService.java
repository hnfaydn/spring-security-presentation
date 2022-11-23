package com.example.springsecuritywithauthorityjwtcrypted.security.token;

import com.example.springsecuritywithauthorityjwtcrypted.security.crypto.CryptoService;
import com.example.springsecuritywithauthorityjwtcrypted.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
import java.time.Instant;

@Slf4j
@Component
public class JwtTokenService implements TokenService {

  private static final String CLAIMS_USER = "user";
  private final JwtProperties jwtProperties;
  private final JwtEncoder jwtEncoder;
  private final JwsHeader jwsHeader;
  private final CryptoService cryptoService;
  private final ObjectMapper objectMapper;

  public JwtTokenService(
      final JwtProperties jwtProperties,
      final JwtEncoder jwtEncoder,
      final CryptoService cryptoService,
      final ObjectMapper objectMapper) {
    this.jwtProperties = jwtProperties;
    this.jwtEncoder = jwtEncoder;
    jwsHeader = JwsHeader.with(jwtProperties::getAlgorithm).build();
    this.cryptoService = cryptoService;
    this.objectMapper = objectMapper;
  }

  @Override
  public String createToken(final User user) {
    final var claims = createClaims(user);
    return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
  }

  private JwtClaimsSet createClaims(final User user) {
    final var now = Instant.now();
    final var expiresAt = now.plus(jwtProperties.getExpiration());
    return JwtClaimsSet.builder()
        .issuer(jwtProperties.getIssuer())
        .issuedAt(now)
        .expiresAt(expiresAt)
        .subject(user.getEmail())
        .claim(CLAIMS_USER, serializeAndEncrypt(user))
        .build();
  }

  @Override
  public User getUser(final Object source) {
    final var claims = ((Jwt) source).getClaims();
    final var encryptedUser = (String) claims.get(CLAIMS_USER);
    return decryptAndDeserialize(encryptedUser);
  }

  private String serializeAndEncrypt(final Object data) {
    try {
      final var json = objectMapper.writeValueAsString(data);
      return cryptoService.encrypt(json);
    } catch (final GeneralSecurityException e) {

      throw new RuntimeException("error");
    } catch (final JsonProcessingException e) {
      throw new RuntimeException("error");
    }
  }

  private User decryptAndDeserialize(final String data) {
    try {
      final var json = cryptoService.decrypt(data);
      return objectMapper.readValue(json, User.class);
    } catch (final GeneralSecurityException e) {
      throw new RuntimeException("error");
    } catch (final JsonProcessingException e) {
      throw new RuntimeException("error");
    }
  }
}
