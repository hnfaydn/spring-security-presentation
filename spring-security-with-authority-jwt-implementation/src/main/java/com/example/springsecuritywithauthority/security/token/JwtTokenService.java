package com.example.springsecuritywithauthority.security.token;

import com.example.springsecuritywithauthority.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtTokenService implements TokenService {

  private static final String CLAIMS_USER = "user";
  private final JwtProperties jwtProperties;
  private final JwtEncoder jwtEncoder;
  private final JwsHeader jwsHeader;
  private final ObjectMapper objectMapper;

  public JwtTokenService(
      final JwtProperties jwtProperties,
      final JwtEncoder jwtEncoder,
      final ObjectMapper objectMapper) {
    this.jwtProperties = jwtProperties;
    this.jwtEncoder = jwtEncoder;
    jwsHeader = JwsHeader.with(jwtProperties::getAlgorithm).build();
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
        .claim(CLAIMS_USER, user)
        .build();
  }

  @Override
  public User getUser(final Object source) {
    final var claims = ((Jwt) source).getClaims();
    final var user = objectMapper.convertValue(claims.get("user"), User.class);
    return user;
  }
}
