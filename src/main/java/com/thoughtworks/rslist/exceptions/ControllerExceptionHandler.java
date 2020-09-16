package com.thoughtworks.rslist.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
  Logger logger = LogManager.getLogger(getClass());

  @ExceptionHandler(RequestParamOutOfBoundsException.class)
  public ResponseEntity handleRequestParamOutOfBoundException(RequestParamOutOfBoundsException e) {
    ErrorComment errorComment = new ErrorComment("invalid request param");
    logger.error(errorComment.getError());
    return ResponseEntity.badRequest().body(errorComment);
  }

  @ExceptionHandler(IndexOutOfBoundsException.class)
  public ResponseEntity handleIndexOutOfBoundsException(IndexOutOfBoundsException e) {
    ErrorComment errorComment = new ErrorComment("invalid index");
    logger.error(errorComment.getError());
    return ResponseEntity.badRequest().body(errorComment);
  }
}
