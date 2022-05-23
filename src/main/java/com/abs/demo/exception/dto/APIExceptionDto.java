package com.abs.demo.exception.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class APIExceptionDto {
  private int status;
  private String code;
  private String message;

}
