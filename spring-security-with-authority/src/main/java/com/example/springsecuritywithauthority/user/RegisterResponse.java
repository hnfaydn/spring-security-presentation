package com.example.springsecuritywithauthority.user;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
  private String firstname;
  private String lastname;
  private String email;
}
