package com.abs.demo.exception.model;

public class NotFoundException extends RuntimeException{
  private String message;

  public NotFoundException(String message) {
    this.message = message;
  }
}