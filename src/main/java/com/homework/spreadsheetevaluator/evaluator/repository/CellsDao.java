package com.homework.spreadsheetevaluator.evaluator.repository;

import com.homework.spreadsheetevaluator.evaluator.dto.Cell;
import com.homework.spreadsheetevaluator.evaluator.dto.CellIdentifier;
import com.homework.spreadsheetevaluator.evaluator.dto.JobIdentifier;
import com.homework.spreadsheetevaluator.evaluator.dto.ValueCell;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class CellsDao {
  private final Map<JobIdentifier, Map<Cell, ValueCell>> evaluatedCells;
  private final Map<JobIdentifier, Map<Cell, String>> errors;

  public CellsDao() {
    this.evaluatedCells = new ConcurrentHashMap<>();
    this.errors = new ConcurrentHashMap<>();
  }

  public ValueCell getCellResult(CellIdentifier cellIdentifier) {
    return getEntry(evaluatedCells, cellIdentifier);
  }

  public void insertCellResult(CellIdentifier cellIdentifier, ValueCell valueCell) {
    insertEntry(evaluatedCells, cellIdentifier, valueCell);
  }

  public String getError(CellIdentifier cellIdentifier) {
    return getEntry(errors, cellIdentifier);
  }

  public void insertError(CellIdentifier cellIdentifier, String error) {
    insertEntry(errors, cellIdentifier, error);
  }

  public boolean isAlreadyEvaluated(CellIdentifier cellIdentifier) {
    return hasEntry(evaluatedCells, cellIdentifier) || hasError(cellIdentifier);
  }

  public boolean hasError(CellIdentifier cellIdentifier) {
    return hasEntry(errors, cellIdentifier);
  }

  private <T> void insertEntry(Map<JobIdentifier, Map<Cell, T>> entries, CellIdentifier cellIdentifier, T value) {
    entries.computeIfAbsent(cellIdentifier.getJobIdentifier(), x -> new ConcurrentHashMap<>())
        .put(cellIdentifier.getCell(), value);
  }

  private <T> T getEntry(Map<JobIdentifier, Map<Cell, T>> entries, CellIdentifier cellIdentifier) {
    return Optional.ofNullable(entries.get(cellIdentifier.getJobIdentifier()))
        .map(x -> x.get(cellIdentifier.getCell()))
        .orElse(null);
  }

  private <T> boolean hasEntry(Map<JobIdentifier, Map<Cell, T>> entries, CellIdentifier cellIdentifier) {
    return Optional.ofNullable(entries.get(cellIdentifier.getJobIdentifier()))
        .map(x -> x.containsKey(cellIdentifier.getCell()))
        .orElse(false);
  }
}
