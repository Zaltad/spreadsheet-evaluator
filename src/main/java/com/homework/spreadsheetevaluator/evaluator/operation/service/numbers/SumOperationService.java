package com.homework.spreadsheetevaluator.evaluator.operation.service.numbers;

import com.homework.spreadsheetevaluator.evaluator.dto.ValueCell;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.Operation;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.ValueCellType;
import com.homework.spreadsheetevaluator.evaluator.operation.service.logical.OperationEvaluator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SumOperationService implements OperationEvaluator {
  public ValueCell evaluate(List<ValueCell> valueCells) {
    return new ValueCell(ValueCellType.NUMBER, valueCells.stream()
        .map(ValueCell::getValue)
        .mapToDouble(x -> (double) x)
        .sum());
  }

  public Operation getSupportedOperation() {
    return Operation.SUM;
  }
}
