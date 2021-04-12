package com.homework.spreadsheetevaluator.evaluator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.homework.spreadsheetevaluator.evaluator.dto.Cell;
import com.homework.spreadsheetevaluator.evaluator.dto.CellIdentifier;
import com.homework.spreadsheetevaluator.evaluator.dto.Job;
import com.homework.spreadsheetevaluator.evaluator.dto.JobIdentifier;
import com.homework.spreadsheetevaluator.evaluator.dto.ValueCell;
import com.homework.spreadsheetevaluator.evaluator.dto.input.JobsInput;
import com.homework.spreadsheetevaluator.evaluator.dto.response.JobsResultsResponse;
import com.homework.spreadsheetevaluator.evaluator.repository.CellsDao;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobsResultsCollectorService {
  private static final String EMAIL = "Zaltad@github.com";

  private final CellsDao cellsDao;
  private final ObjectMapper objectMapper;

  public JobsResultsCollectorService(CellsDao cellsDao,
      ObjectMapper objectMapper) {
    this.cellsDao = cellsDao;
    this.objectMapper = objectMapper;
  }

  public JobsResultsResponse collectJobsResults(JobsInput jobsInput) {
    List<Job> jobs = jobsInput.getJobs().stream()
        .map(jobInput -> new Job(jobInput.getId(), collectJobResults(new JobIdentifier(jobsInput.getSubmissionUrl(), jobInput.getId()), jobInput.getData())))
        .collect(Collectors.toList());
    return new JobsResultsResponse(EMAIL, jobs);
  }

  private List<List<JsonNode>> collectJobResults(JobIdentifier jobIdentifier, List<List<JsonNode>> data) {
    List<List<JsonNode>> result = new ArrayList<>();
    for (int i = 0; i < data.size(); i++) {
      result.add(new ArrayList<>());
      for (int j = 0; j < data.get(i).size(); j++) {
        Cell cell = new Cell(i, j);
        CellIdentifier cellIdentifier = new CellIdentifier(jobIdentifier, cell);
        String error = cellsDao.getError(cellIdentifier);
        if (error != null) {
          result.get(i).add(objectMapper.createObjectNode().put("error", error));
        } else {
          result.get(i).add(objectMapper.createObjectNode().set("value", getValueCellNode(cellsDao.getCellResult(cellIdentifier))));
        }
      }
    }
    return result;
  }

  private JsonNode getValueCellNode(ValueCell valueCell) {
    ObjectNode jsonNode = objectMapper.createObjectNode();
    String property = valueCell.getType().getProperty();
    switch (valueCell.getType()) {
      case NUMBER:
        jsonNode.put(property, (double) valueCell.getValue());
        break;
      case TEXT:
        jsonNode.put(property, (String) valueCell.getValue());
        break;
      case BOOLEAN:
        jsonNode.put(property, (boolean) valueCell.getValue());
        break;
      default:
        throw new IllegalArgumentException("Unrecognized value cell type");
    }
    return jsonNode;
  }
}
