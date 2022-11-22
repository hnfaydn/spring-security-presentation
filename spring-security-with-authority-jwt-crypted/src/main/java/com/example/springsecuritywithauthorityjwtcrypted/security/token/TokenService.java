package com.example.springsecuritywithauthorityjwtcrypted.security.token;


import com.example.springsecuritywithauthorityjwtcrypted.user.User;

public interface TokenService {
  String createToken(User user);

  User getUser(Object source);
}
