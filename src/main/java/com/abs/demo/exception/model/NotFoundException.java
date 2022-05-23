package com.abs.demo.exception.model;

public class NotFoundException extends RuntimeException{
  private String message;

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
