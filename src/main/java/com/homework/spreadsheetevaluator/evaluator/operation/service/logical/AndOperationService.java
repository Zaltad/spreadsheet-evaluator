package com.homework.spreadsheetevaluator.evaluator.operation.service.logical;

import com.homework.spreadsheetevaluator.evaluator.dto.ValueCell;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.Operation;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.ValueCellType;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AndOperationService implements OperationEvaluator {
  public ValueCell evaluate(List<ValueCell> valueCells) {
    return new ValueCell(ValueCellType.BOOLEAN, valueCells.stream()
        .map(ValueCell::getValue)
        .reduce(true, (a, b) -> (boolean) a && (boolean) b));
  }

  public Operation getSupportedOperation() {
    return Operation.AND;
  }
}
