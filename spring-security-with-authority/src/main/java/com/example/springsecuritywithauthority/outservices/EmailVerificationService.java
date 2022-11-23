package com.example.springsecuritywithauthority.outservices;

public interface EmailVerificationService {
  boolean emailVerification(String email);
}
