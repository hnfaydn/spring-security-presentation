package com.example.springsecuritywithauthorityjwtcrypted.outservices;

public interface EmailVerificationService {
  boolean emailVerification(String email);
}
