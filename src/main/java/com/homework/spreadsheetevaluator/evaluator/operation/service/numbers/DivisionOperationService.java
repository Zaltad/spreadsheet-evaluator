package com.homework.spreadsheetevaluator.evaluator.operation.service.numbers;

import com.homework.spreadsheetevaluator.evaluator.dto.ValueCell;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.Operation;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.ValueCellType;
import com.homework.spreadsheetevaluator.evaluator.operation.service.logical.OperationEvaluator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class DivisionOperationService implements OperationEvaluator {
  public ValueCell evaluate(List<ValueCell> valueCells) {
    double firstOperand = (double) valueCells.get(0).getValue();
    double secondOperand = (double) valueCells.get(1).getValue();
    Assert.isTrue(secondOperand != 0, "Second operand of division operation cannot be zero");
    return new ValueCell(ValueCellType.NUMBER, firstOperand / secondOperand);
  }

  public Operation getSupportedOperation() {
    return Operation.DIV;
  }
}
