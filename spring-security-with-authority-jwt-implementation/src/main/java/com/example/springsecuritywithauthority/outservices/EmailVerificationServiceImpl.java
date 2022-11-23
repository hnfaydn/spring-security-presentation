package com.example.springsecuritywithauthority.outservices;

import org.springframework.stereotype.Service;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {
    @Override
    public boolean emailVerification(String email) {
        return true;
    }
}
