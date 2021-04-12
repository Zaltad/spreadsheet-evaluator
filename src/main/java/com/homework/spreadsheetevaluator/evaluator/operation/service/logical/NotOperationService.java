package com.homework.spreadsheetevaluator.evaluator.operation.service.logical;

import com.homework.spreadsheetevaluator.evaluator.dto.ValueCell;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.Operation;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.ValueCellType;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NotOperationService implements OperationEvaluator {
  public ValueCell evaluate(List<ValueCell> valueCells) {
    return new ValueCell(ValueCellType.BOOLEAN, !((boolean) valueCells.get(0).getValue()));
  }

  public Operation getSupportedOperation() {
    return Operation.NEG;
  }
}
