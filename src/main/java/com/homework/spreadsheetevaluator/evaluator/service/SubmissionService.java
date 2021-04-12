package com.homework.spreadsheetevaluator.evaluator.service;

import com.homework.spreadsheetevaluator.evaluator.dto.input.JobsInput;
import com.homework.spreadsheetevaluator.evaluator.dto.response.JobsResultsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SubmissionService {
  private static final String PATH = "/jobs";
  private final RestTemplate restTemplate;

  public SubmissionService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public JobsInput retrieveJobsForEvaluation() {
    ResponseEntity<JobsInput> response;
    try {
      response = restTemplate.getForEntity(PATH, JobsInput.class);
    } catch (Exception e) {
      log.error("Exception occurred during the call for jobs", e);
      throw new RuntimeException(e);
    }
    return response.getBody();
  }

  public String submitJobsForEvaluation(String submissionUrl, JobsResultsResponse jobsResultsResponse) {
    ResponseEntity<String> response;
    try {
      response = restTemplate.postForEntity(submissionUrl, jobsResultsResponse, String.class);
    } catch (Exception e) {
      log.error("Exception occurred during the submission of jobs results", e);
      throw new RuntimeException(e);
    }
    return response.getBody();
  }
}
