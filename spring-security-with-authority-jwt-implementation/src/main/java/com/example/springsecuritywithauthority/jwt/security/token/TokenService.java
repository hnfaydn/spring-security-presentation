package com.example.springsecuritywithauthority.jwt.security.token;

import com.example.springsecuritywithauthority.jwt.user.User;

public interface TokenService {
  String createToken(User user);

  User getUser(Object source);
}
