package com.thoughtworks.rslist;

import com.thoughtworks.rslist.exception.RequestParamOutOfBoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
  @ExceptionHandler(RequestParamOutOfBoundException.class)
  public ResponseEntity handleRequestParamOutOfBoundException(RequestParamOutOfBoundException e) {
    ErrorComment errorComment = new ErrorComment("invalid request param");
    return ResponseEntity.badRequest().body(errorComment);
  }
}
