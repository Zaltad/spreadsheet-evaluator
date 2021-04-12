package com.homework.spreadsheetevaluator.evaluator.operation.service.logical;

import com.homework.spreadsheetevaluator.evaluator.dto.ValueCell;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.Operation;
import java.util.List;

public interface OperationEvaluator {
  ValueCell evaluate(List<ValueCell> valueCells);
  Operation getSupportedOperation();
}
