package com.example.springsecuritywithauthority.jwt.user;

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
    private String token;
}
