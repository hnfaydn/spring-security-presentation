package com.example.basicauthentication.configs;

import com.example.basicauthentication.user.UserDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomUserDetailService implements UserDetailsService {
  private final UserDao userDao;

  public CustomUserDetailService(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    com.example.basicauthentication.user.User user = this.userDao.findUserByEmail(email);
    if (Objects.isNull(user)) {
      throw new UsernameNotFoundException("There is no user with following email adress:" + email);
    }
    return new MyUserDetails(user);
  }
}
