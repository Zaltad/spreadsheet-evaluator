package com.homework.spreadsheetevaluator.evaluator.controller;

import com.homework.spreadsheetevaluator.evaluator.dto.input.JobsInput;
import com.homework.spreadsheetevaluator.evaluator.dto.response.JobsResultsResponse;
import com.homework.spreadsheetevaluator.evaluator.service.JobsFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("evaluator")
public class EvaluatorController {
  private final JobsFacade jobsFacade;

  public EvaluatorController(
      JobsFacade jobsFacade) {
    this.jobsFacade = jobsFacade;
  }

  @PostMapping
  public JobsResultsResponse evaluate(@RequestBody JobsInput jobsInput) {
    return jobsFacade.evaluateJobs(jobsInput);
  }

  @GetMapping("submission")
  public String evaluateRetrievedJobs() {
    return jobsFacade.evaluateRetrievedJobs();
  }
}
