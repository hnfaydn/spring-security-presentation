package com.example.springsecuritywithauthority.jwt.user;

import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<RegisterResponse> register(RegisterRequest registerRequest);

    ResponseEntity<String> adminAuthorityAcceptOnly();

    ResponseEntity<String> userAuthorityAcceptOnly();

    ResponseEntity<String> editorAuthorityAcceptOnly();

    ResponseEntity<String> acceptsAnyAuthority();
}
