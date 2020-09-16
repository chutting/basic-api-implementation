package com.thoughtworks.rslist.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
  @ExceptionHandler(RequestParamOutOfBoundsException.class)
  public ResponseEntity handleRequestParamOutOfBoundException(RequestParamOutOfBoundsException e) {
    ErrorComment errorComment = new ErrorComment("invalid request param");
    return ResponseEntity.badRequest().body(errorComment);
  }

  @ExceptionHandler(IndexOutOfBoundsException.class)
  public ResponseEntity handleIndexOutOfBoundsException(IndexOutOfBoundsException e) {
    ErrorComment errorComment = new ErrorComment("invalid index");
    return ResponseEntity.badRequest().body(errorComment);
  }
}
