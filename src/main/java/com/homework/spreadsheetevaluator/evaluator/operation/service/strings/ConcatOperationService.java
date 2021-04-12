package com.homework.spreadsheetevaluator.evaluator.operation.service.strings;

import com.homework.spreadsheetevaluator.evaluator.dto.ValueCell;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.Operation;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.ValueCellType;
import com.homework.spreadsheetevaluator.evaluator.operation.service.logical.OperationEvaluator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ConcatOperationService implements OperationEvaluator {
  public ValueCell evaluate(List<ValueCell> valueCells) {
    return new ValueCell(ValueCellType.TEXT, valueCells.stream()
        .map(valueCell -> (String) valueCell.getValue())
        .collect(Collectors.joining()));
  }

  public Operation getSupportedOperation() {
    return Operation.CONCAT;
  }
}
