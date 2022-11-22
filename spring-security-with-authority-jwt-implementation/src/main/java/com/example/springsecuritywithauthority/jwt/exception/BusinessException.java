package com.example.springsecuritywithauthority.jwt.exception;

public class BusinessException extends RuntimeException {
  public BusinessException(String message) {
    super(message);
  }
}
