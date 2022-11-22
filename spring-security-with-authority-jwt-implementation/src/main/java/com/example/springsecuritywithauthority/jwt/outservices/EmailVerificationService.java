package com.example.springsecuritywithauthority.jwt.outservices;

public interface EmailVerificationService {
    boolean emailVerification(String email);
}
