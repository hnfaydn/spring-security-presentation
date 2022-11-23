package com.example.springsecuritywithauthority.security.token;

import com.example.springsecuritywithauthority.user.User;

public interface TokenService {
  String createToken(User user);

  User getUser(Object source);
}
