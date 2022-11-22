package com.example.springsecuritywithauthority.jwt.user;

import com.example.springsecuritywithauthority.jwt.exception.BusinessException;
import com.example.springsecuritywithauthority.jwt.outservices.EmailVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {
  private final UserDao userDao;
  private final EmailVerificationService emailVerificationService;

  public UserServiceImpl(UserDao userDao, EmailVerificationService emailVerificationService) {
    this.userDao = userDao;
    this.emailVerificationService = emailVerificationService;
  }

  @Override
  public ResponseEntity<RegisterResponse> register(RegisterRequest registerRequest) {

    CheckEmailIsAlreadyUsing(registerRequest.getEmail());
    emailVerificationService.emailVerification(registerRequest.getEmail());
    User user =
        User.builder()
            .id(0)
            .firstname(registerRequest.getFirstname())
            .lastname(registerRequest.getLastname())
            .email(registerRequest.getEmail())
            .password(registerRequest.getPassword())
            .authority(registerRequest.getAuthority().toUpperCase(Locale.ENGLISH))
            .build();

    RegisterResponse registerResponse =
        RegisterResponse.builder()
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .email(user.getEmail())
            .build();

    this.userDao.save(user);
    return new ResponseEntity<>(registerResponse, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> adminAuthorityAcceptOnly() {
    return new ResponseEntity<>("Admin authorization has been succeed", HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> userAuthorityAcceptOnly() {
    return new ResponseEntity<>("User authorization has been succeed", HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> editorAuthorityAcceptOnly() {
    return new ResponseEntity<>("Editor authorization has been succeed", HttpStatus.OK);
  }

  @Override
  public ResponseEntity<String> acceptsAnyAuthority() {
    return new ResponseEntity<>("Accepts any of authorities", HttpStatus.OK);
  }

  private void CheckEmailIsAlreadyUsing(String email) {
    if (this.userDao.existsByEmail(email)) {
      throw new BusinessException("This is email is already using");
    }
  }
}
