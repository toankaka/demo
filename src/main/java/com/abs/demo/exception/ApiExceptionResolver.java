package com.abs.demo.exception;

import com.abs.demo.exception.model.BadParamsException;
import com.abs.demo.exception.model.NotFoundException;
import java.util.function.Supplier;
import javax.servlet.ServletException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class ApiExceptionResolver {
  private Supplier<String> badParamsExceptionCode = () -> "001001";
  private Supplier<String> unhandledExceptionCode = () -> "001001";
  private Supplier<String> notFoundExceptionCode = () -> "001002";

  public String resolve(Exception e) {

    //400
    if (e instanceof BadParamsException || e instanceof ServletException
        || e instanceof MethodArgumentNotValidException) {
      return badParamsExceptionCode.get();
    }
    //404
    else if (e instanceof NotFoundException) {
      return notFoundExceptionCode.get();
    }else {
      return unhandledExceptionCode.get();
    }
  }
}
