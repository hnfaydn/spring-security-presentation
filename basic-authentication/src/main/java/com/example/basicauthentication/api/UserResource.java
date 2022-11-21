package com.example.basicauthentication.api;

import com.example.basicauthentication.user.UserService;
import com.example.basicauthentication.user.requests.LoginRequestBody;
import com.example.basicauthentication.user.responses.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserResource {

  private final UserService userService;

  public UserResource(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    final var loginRequestBody = new LoginRequestBody();
    loginRequestBody.setEmail(loginRequest.getEmail());
    loginRequestBody.setPassword(loginRequest.getPassword());
    return this.userService.login(loginRequestBody);
  }

  @GetMapping("/get")
  String login() {
    return "Security work correctly";
  }
}
