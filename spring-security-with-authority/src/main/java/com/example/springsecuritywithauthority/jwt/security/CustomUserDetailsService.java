package com.example.springsecuritywithauthority.jwt.security;

import com.example.springsecuritywithauthority.jwt.user.User;
import com.example.springsecuritywithauthority.jwt.user.UserDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserDao userDao;

  public CustomUserDetailsService(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = this.userDao.findUserByEmail(email);
    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("There is no user with following email adress:" + email);
    }
    return new MyUserDetails(user);
  }
}
