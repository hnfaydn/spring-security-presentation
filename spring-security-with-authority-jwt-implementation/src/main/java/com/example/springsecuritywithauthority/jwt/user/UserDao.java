package com.example.springsecuritywithauthority.jwt.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    User findUserByEmail(String email);
}
