package com.abs.demo.exception;

import com.abs.demo.exception.dto.APIExceptionDto;
import com.abs.demo.exception.model.BadParamsException;
import com.abs.demo.exception.model.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  private final ApiExceptionResolver apiExceptionResolver;

  public ApiExceptionHandler(ApiExceptionResolver apiExceptionResolver) {
    this.apiExceptionResolver = apiExceptionResolver;
  }

  /**
   * 500 Internal server error
   */
  @ExceptionHandler({Exception.class})
  protected ResponseEntity<Object> handleAll(Exception e) {
    APIExceptionDto dto = APIExceptionDto.builder()
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .code("500")
        .message(e.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto);
  }

  /**
   * 400 Bad Request
   */
  @ExceptionHandler(BadParamsException.class)
  protected ResponseEntity<Object> handleBadParam(Exception e){
    APIExceptionDto dto = APIExceptionDto.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .code("400")
        .message(e.getMessage())
        .build();
    return ResponseEntity.badRequest()
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto);
  }

  /**
   * 404 Not Found
   */
  @ExceptionHandler(NotFoundException.class)
  protected ResponseEntity<Object> handleNotFound(Exception e){
    APIExceptionDto dto = APIExceptionDto.builder()
        .status(HttpStatus.NOT_FOUND.value())
        .code("404")
        .message(e.getMessage())
        .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .contentType(MediaType.APPLICATION_JSON)
        .body(dto);
  }
}
