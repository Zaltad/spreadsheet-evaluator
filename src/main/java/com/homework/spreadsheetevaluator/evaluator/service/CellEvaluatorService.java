package com.homework.spreadsheetevaluator.evaluator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.homework.spreadsheetevaluator.evaluator.dto.Cell;
import com.homework.spreadsheetevaluator.evaluator.dto.CellIdentifier;
import com.homework.spreadsheetevaluator.evaluator.dto.JobIdentifier;
import com.homework.spreadsheetevaluator.evaluator.dto.ValueCell;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.Operation;
import com.homework.spreadsheetevaluator.evaluator.exception.CyclicDependencyException;
import com.homework.spreadsheetevaluator.evaluator.operation.service.logical.OperationEvaluator;
import com.homework.spreadsheetevaluator.evaluator.repository.CellsDao;
import com.homework.spreadsheetevaluator.evaluator.repository.JobsDao;
import com.homework.spreadsheetevaluator.evaluator.utils.ReferenceParser;
import com.homework.spreadsheetevaluator.evaluator.utils.ValueParser;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Slf4j
public class CellEvaluatorService {
  private static final String REFERENCE_TYPE = "reference";
  private static final String FORMULA_TYPE = "formula";
  private final CellsDao cellsDao;
  private final JobsDao jobsDao;
  private final Map<Operation, OperationEvaluator> operationEvaluators;

  public CellEvaluatorService(CellsDao cellsDao, JobsDao jobsDao,
      List<OperationEvaluator> operationEvaluators) {
    this.cellsDao = cellsDao;
    this.jobsDao = jobsDao;
    this.operationEvaluators = operationEvaluators.stream()
        .collect(Collectors.toMap(OperationEvaluator::getSupportedOperation, Function.identity()));
  }

  public void evaluateExpression(CellIdentifier cellIdentifier, Set<Cell> visitedCells) {
    try {
      JsonNode jsonNode = jobsDao.getCell(cellIdentifier);
      ValueCell valueCell = evaluateCell(jsonNode, cellIdentifier.getJobIdentifier(), visitedCells);
      cellsDao.insertCellResult(cellIdentifier, valueCell);
    } catch (Exception e) {
      log.error(
          "Error occured while evaluating the cell for submission url {}, job id {}, row {} and cell {}",
          cellIdentifier.getJobIdentifier().getSubmissionUrl(),
          cellIdentifier.getJobIdentifier().getJobId(), cellIdentifier.getCell().getRow(),
          cellIdentifier.getCell().getColumn(), e);
      cellsDao.insertError(cellIdentifier, e.getMessage());
    }
  }

  private ValueCell evaluateCell(JsonNode jsonNode, JobIdentifier jobIdentifier,
      Set<Cell> visitedCells) {
    Assert.isTrue(jsonNode.size() == 1, "Node can't have more than one property");
    String fieldName = jsonNode.fieldNames().next();
    switch (fieldName) {
      case ValueParser.VALUE_TYPE:
        return ValueParser.parseValueCell(jsonNode);
      case REFERENCE_TYPE:
        return getReferenceValue(jsonNode, jobIdentifier, visitedCells);
      case FORMULA_TYPE:
        return getFormulaCell(jsonNode, jobIdentifier, visitedCells);
      default:
        return applyOperation(jsonNode, fieldName, jobIdentifier, visitedCells);
    }
  }

  private ValueCell applyOperation(JsonNode jsonNode, String fieldName, JobIdentifier jobIdentifier,
      Set<Cell> visitedCells) {
    Operation operation = Operation.getByLabel(fieldName);
    Assert.isTrue(operation != null, "Invalid operation");
    List<ValueCell> values = parseValues(jsonNode.at(operation.getPath()), operation, jobIdentifier, visitedCells);
    return operationEvaluators.get(operation).evaluate(values);
  }

  private ValueCell getFormulaCell(JsonNode jsonNode, JobIdentifier jobIdentifier,
      Set<Cell> visitedCells) {
    JsonNode formulaNode = jsonNode.at("/" + FORMULA_TYPE);
    Assert.isTrue(formulaNode.size() == 1, "Formula doesn't have single node");
    return evaluateCell(formulaNode, jobIdentifier, visitedCells);
  }

  private void checkForCyclicDependencies(Set<Cell> visitedCells, Cell cell) {
    if (visitedCells.contains(cell)) {
      log.error("Cyclic dependency detected for cell with row {} and column {}", cell.getRow(),
          cell.getColumn());
      throw new CyclicDependencyException(String
          .format("Cyclic dependency detected for cell %d and column %d", cell.getRow(),
              cell.getColumn()));
    }
  }

  private ValueCell getReferenceValue(JsonNode jsonNode, JobIdentifier jobIdentifier,
      Set<Cell> visitedCells) {
    JsonNode referenceNode = jsonNode.at("/" + REFERENCE_TYPE);
    Assert.isTrue(referenceNode.isTextual(), "Reference is not text");
    Cell cell = ReferenceParser.matchReference(referenceNode.asText());
    Assert
        .isTrue(jobsDao.isValidJobReference(jobIdentifier, cell), "Reference to non-existing cell");
    checkForCyclicDependencies(visitedCells, cell);
    CellIdentifier cellIdentifier = new CellIdentifier(jobIdentifier, cell);
    if (!cellsDao.isAlreadyEvaluated(cellIdentifier)) {
      evaluateExpression(cellIdentifier, Stream.concat(visitedCells.stream(), Stream.of(cell))
          .collect(Collectors.toCollection(HashSet::new)));
    }
    if (cellsDao.hasError(cellIdentifier)) {
      throw new IllegalArgumentException(String
          .format("Error detected for row %d, column %d: %s", cell.getRow(), cell.getColumn(),
              cellsDao
                  .getError(cellIdentifier)));
    }
    return cellsDao.getCellResult(cellIdentifier);
  }

  private List<ValueCell> parseValues(JsonNode jsonNode, Operation operation, JobIdentifier jobIdentifier, Set<Cell> visitedCells) {
    Integer numberOfOperands = operation.getNumberOfOperands();
    List<Integer> validatableIndices = operation.getValidatableIndices();
    Assert.isTrue(numberOfOperands == null || jsonNode.size() == numberOfOperands,
        "Found another amount of operands than expected");
    List<ValueCell> valueCells = (numberOfOperands != null && numberOfOperands == 1) ? List
        .of(evaluateCell(jsonNode, jobIdentifier, visitedCells))
        : StreamSupport.stream(jsonNode.spliterator(), false)
            .map(node -> evaluateCell(node, jobIdentifier, visitedCells))
            .collect(Collectors.toList());
    if (validatableIndices == null) {
      Assert.isTrue(valueCells.stream()
          .map(ValueCell::getType)
          .allMatch(type -> type == valueCells.get(0).getType()), "Types incompatibility");
    }
    Assert.isTrue(IntStream.range(0, valueCells.size())
        .filter(i -> validatableIndices == null || validatableIndices.contains(i))
        .allMatch(i -> operation.getAllowedTypes().contains(valueCells.get(i).getType())), "Not matching expected types");
    return valueCells;
  }
}
