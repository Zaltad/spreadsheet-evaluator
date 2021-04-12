package com.homework.spreadsheetevaluator.evaluator.operation.service.comparison;

import com.homework.spreadsheetevaluator.evaluator.dto.ValueCell;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.Operation;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.ValueCellType;
import com.homework.spreadsheetevaluator.evaluator.operation.service.logical.OperationEvaluator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EqualsOperationService implements OperationEvaluator {
  public ValueCell evaluate(List<ValueCell> valueCells) {
    ValueCell firstOperand = valueCells.get(0);
    ValueCell secondOperand = valueCells.get(1);
    switch (firstOperand.getType()) {
      case NUMBER:
        return new ValueCell(ValueCellType.BOOLEAN, (double) firstOperand.getValue() == (double) secondOperand.getValue());
      case TEXT:
        return new ValueCell(ValueCellType.BOOLEAN, firstOperand.getValue().equals(secondOperand.getValue()));
      default:
        throw new IllegalArgumentException("Illegal type for is equal operation");
    }
  }

  public Operation getSupportedOperation() {
    return Operation.EQ;
  }
}
