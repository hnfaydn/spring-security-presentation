package com.example.basicauthentication;

import com.example.basicauthentication.user.User;
import com.example.basicauthentication.user.UserDao;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
  private final UserDao userDao;

  public ApplicationStartup(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    final var user = new User(1, "testFirstname", "testLastname", "test@mail.com", "test");
    final var user2 = new User(2, "testFirstname", "testLastname", "testMail", "test");
    final var userList = List.of(user, user2);
    this.userDao.saveAll(userList);
  }
}
