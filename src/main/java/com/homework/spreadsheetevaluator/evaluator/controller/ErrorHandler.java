package com.homework.spreadsheetevaluator.evaluator.controller;

import com.homework.spreadsheetevaluator.evaluator.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler
  public ErrorResponse handleException(Exception e) {
    log.error(e.getMessage(), e);
    return new ErrorResponse(e.getMessage());
  }
}
