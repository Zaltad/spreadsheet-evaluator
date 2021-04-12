package com.homework.spreadsheetevaluator.evaluator.utils;

import com.homework.spreadsheetevaluator.evaluator.dto.Cell;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReferenceParser {
  private static final Pattern REFERENCE_PATTERN = Pattern.compile("([A-Z])(\\d+)");;

  public static Cell matchReference(String reference) {
    Matcher matcher = REFERENCE_PATTERN.matcher(reference);
    if (matcher.find()) {
      int row = Integer.parseInt(matcher.group(2));
      char column = matcher.group(1).charAt(0);
      return new Cell(row - 1, CellIndexParser.convertFromColumnNotation(column));
    }
    throw new IllegalArgumentException("Illegal reference format");
  }
}
