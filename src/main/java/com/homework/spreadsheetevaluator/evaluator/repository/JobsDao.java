package com.homework.spreadsheetevaluator.evaluator.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.homework.spreadsheetevaluator.evaluator.dto.Cell;
import com.homework.spreadsheetevaluator.evaluator.dto.CellIdentifier;
import com.homework.spreadsheetevaluator.evaluator.dto.JobIdentifier;
import com.homework.spreadsheetevaluator.evaluator.dto.input.JobsInput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class JobsDao {
  private final Map<CellIdentifier, JsonNode> cells;
  private final Map<JobIdentifier, List<Integer>> jobBoundaries;

  public JobsDao() {
    this.jobBoundaries = new ConcurrentHashMap<>();
    this.cells = new ConcurrentHashMap<>();
  }

  public JsonNode getCell(CellIdentifier cellIdentifier) {
    return cells.get(cellIdentifier);
  }

  public void insertCellsForJobs(JobsInput jobsInput) {
    jobsInput.getJobs()
        .forEach(job -> insertCells(new JobIdentifier(jobsInput.getSubmissionUrl(), job.getId()), job.getData()));
  }

  private void insertCells(JobIdentifier jobIdentifier, List<List<JsonNode>> data) {
    for (int i = 0; i < data.size(); i++) {
      jobBoundaries.computeIfAbsent(jobIdentifier, x -> new ArrayList<>()).add(data.get(i).size());
      for (int j = 0; j < data.get(i).size(); j++) {
        cells.put(new CellIdentifier(jobIdentifier, new Cell(i, j)), data.get(i).get(j));
      }
    }
  }

  public boolean isValidJobReference(JobIdentifier jobIdentifier, Cell cell) {
    List<Integer> jobBoundary = jobBoundaries.get(jobIdentifier);
    int row = cell.getRow();
    return row < jobBoundary.size() && cell.getColumn() < jobBoundary.get(row);
  }
}
