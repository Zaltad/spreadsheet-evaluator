package com.homework.spreadsheetevaluator.evaluator.dto;

import lombok.Value;

@Value
public class CellIdentifier {
  JobIdentifier jobIdentifier;
  Cell cell;
}
