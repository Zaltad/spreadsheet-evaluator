package com.homework.spreadsheetevaluator.evaluator.utils;

public class CellIndexParser {
  public static char convertColumn(int index) {
    return (char) ('A' + index);
  }

  public static int convertFromColumnNotation(char columnNotation) {
    return columnNotation - 'A';
  }
}
