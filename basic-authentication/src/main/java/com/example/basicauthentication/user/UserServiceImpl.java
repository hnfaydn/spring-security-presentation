package com.example.basicauthentication.user;

import com.example.basicauthentication.user.requests.LoginRequestBody;
import com.example.basicauthentication.user.responses.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
  private final UserDao userDao;

  public UserServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public ResponseEntity<LoginResponse> login(LoginRequestBody loginRequestBody) {
    final var userByEmail = Optional.of(this.userDao.findUserByEmail(loginRequestBody.getEmail()));

    final var user =
        userByEmail.orElseThrow(
            () -> {
              throw new UsernameNotFoundException(
                  "There is no user with following email: " + loginRequestBody.getEmail());
            });

    return new ResponseEntity<>(
        new LoginResponse(user.getEmail(), user.getFirstname(), user.getLastname()), HttpStatus.OK);
  }
}
