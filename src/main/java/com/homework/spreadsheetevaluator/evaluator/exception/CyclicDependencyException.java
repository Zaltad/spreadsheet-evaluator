package com.homework.spreadsheetevaluator.evaluator.exception;

public class CyclicDependencyException extends RuntimeException {
  public CyclicDependencyException() {
    super();
  }

  public CyclicDependencyException(String message) {
    super(message);
  }
}
