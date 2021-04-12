package com.homework.spreadsheetevaluator.evaluator.operation.service.logical;

import com.homework.spreadsheetevaluator.evaluator.dto.ValueCell;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.Operation;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class IfOperationService implements OperationEvaluator {
  public ValueCell evaluate(List<ValueCell> valueCells) {
    boolean condition = (boolean) valueCells.get(0).getValue();
    ValueCell firstOperand = valueCells.get(1);
    ValueCell secondOperand = valueCells.get(2);
    return condition ? firstOperand : secondOperand;
  }

  public Operation getSupportedOperation() {
    return Operation.IF;
  }
}
