package com.homework.spreadsheetevaluator.evaluator.dto.enums;

import java.util.List;
import java.util.stream.Stream;

public enum Operation {
  SUM("sum", List.of(ValueCellType.NUMBER)),
  MUL("multiply", List.of(ValueCellType.NUMBER)),
  DIV("divide", List.of(ValueCellType.NUMBER), 2),
  GT("is_greater", List.of(ValueCellType.NUMBER, ValueCellType.TEXT), 2),
  EQ("is_equal", List.of(ValueCellType.NUMBER, ValueCellType.TEXT), 2),
  NEG("not", List.of(ValueCellType.BOOLEAN), 1),
  AND("and", List.of(ValueCellType.BOOLEAN)),
  OR("or", List.of(ValueCellType.BOOLEAN)),
  IF("if", List.of(ValueCellType.BOOLEAN), 3, List.of(0)),
  CONCAT("concat", List.of(ValueCellType.TEXT));

  Operation(String label, List<ValueCellType> allowedTypes, Integer numberOfOperands, List<Integer> validatableIndices) {
    this.label = label;
    this.allowedTypes = allowedTypes;
    this.numberOfOperands = numberOfOperands;
    this.validatableIndices = validatableIndices;
  }

  Operation(String label, List<ValueCellType> allowedTypes, Integer numberOfOperands) {
    this.label = label;
    this.allowedTypes = allowedTypes;
    this.numberOfOperands = numberOfOperands;
    this.validatableIndices = null;
  }

  Operation(String label, List<ValueCellType> allowedTypes) {
    this.label = label;
    this.allowedTypes = allowedTypes;
    this.numberOfOperands = null;
    this.validatableIndices = null;
  }

  private final String label;
  private final List<ValueCellType> allowedTypes;
  private final Integer numberOfOperands;
  private final List<Integer> validatableIndices;

  public String getLabel() {
    return label;
  }

  public String getPath() {
    return "/" + label;
  }

  public List<ValueCellType> getAllowedTypes() {
    return allowedTypes;
  }

  public Integer getNumberOfOperands() {
    return numberOfOperands;
  }

  public List<Integer> getValidatableIndices() {
    return validatableIndices;
  }

  public static Operation getByLabel(String label) {
    return Stream.of(values())
        .filter(operation -> operation.getLabel().equals(label))
        .findAny()
        .orElse(null);
  }
}
