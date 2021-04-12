package com.homework.spreadsheetevaluator.evaluator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.homework.spreadsheetevaluator.evaluator.dto.Cell;
import com.homework.spreadsheetevaluator.evaluator.dto.CellIdentifier;
import com.homework.spreadsheetevaluator.evaluator.dto.JobIdentifier;
import com.homework.spreadsheetevaluator.evaluator.dto.input.JobsInput;
import com.homework.spreadsheetevaluator.evaluator.repository.CellsDao;
import com.homework.spreadsheetevaluator.evaluator.repository.JobsDao;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobsEvaluatorService {
  private final CellsDao cellsDao;
  private final JobsDao jobsDao;
  private final CellEvaluatorService cellEvaluatorService;

  public JobsEvaluatorService(CellsDao cellsDao, JobsDao jobsDao,
      CellEvaluatorService cellEvaluatorService) {
    this.cellsDao = cellsDao;
    this.jobsDao = jobsDao;
    this.cellEvaluatorService = cellEvaluatorService;
  }

  public void evaluateJobs(JobsInput jobsInput) {
    jobsDao.insertCellsForJobs(jobsInput);
    jobsInput.getJobs()
        .forEach(job -> evaluateJob(new JobIdentifier(jobsInput.getSubmissionUrl(), job.getId()), job.getData()));
  }

  private void evaluateJob(JobIdentifier jobIdentifier, List<List<JsonNode>> data) {
    for (int i = 0; i < data.size(); i++) {
      for (int j = 0; j < data.get(i).size(); j++) {
        Cell cell = new Cell(i, j);
        CellIdentifier cellIdentifier = new CellIdentifier(jobIdentifier, cell);
        if (!cellsDao.isAlreadyEvaluated(cellIdentifier)) {
          cellEvaluatorService.evaluateExpression(cellIdentifier, Stream.of(cell)
              .collect(Collectors.toCollection(HashSet::new)));
        }
      }
    }
  }
}
