package com.homework.spreadsheetevaluator.evaluator.dto;

import com.homework.spreadsheetevaluator.evaluator.dto.enums.ValueCellType;
import lombok.Value;

@Value
public class ValueCell {
  ValueCellType type;
  Object value;
}
