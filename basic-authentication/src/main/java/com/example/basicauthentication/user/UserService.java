package com.example.basicauthentication.user;

import com.example.basicauthentication.user.requests.LoginRequestBody;
import com.example.basicauthentication.user.responses.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
  ResponseEntity<LoginResponse> login(LoginRequestBody loginRequestBody);
}
