package com.abs.demo.exception.model;

public class BadParamsException extends RuntimeException{
  private String message;

  public BadParamsException(String message) {
    this.message = message;
  }
}
