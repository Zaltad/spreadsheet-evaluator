package com.homework.spreadsheetevaluator.evaluator.service;

import com.homework.spreadsheetevaluator.evaluator.dto.input.JobsInput;
import com.homework.spreadsheetevaluator.evaluator.dto.response.JobsResultsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobsFacade {
  private final JobsEvaluatorService jobsEvaluatorService;
  private final SubmissionService submissionService;
  private final JobsResultsCollectorService jobsResultsCollectorService;

  public JobsFacade(JobsEvaluatorService jobsEvaluatorService,
      SubmissionService submissionService,
      JobsResultsCollectorService jobsResultsCollectorService) {
    this.jobsEvaluatorService = jobsEvaluatorService;
    this.submissionService = submissionService;
    this.jobsResultsCollectorService = jobsResultsCollectorService;
  }

  public String evaluateRetrievedJobs() {
    JobsInput jobsInput = submissionService.retrieveJobsForEvaluation();
    JobsResultsResponse jobsResultsResponse = evaluateJobs(jobsInput);
    return submissionService.submitJobsForEvaluation(jobsInput.getSubmissionUrl(), jobsResultsResponse);
  }

  public JobsResultsResponse evaluateJobs(JobsInput jobsInput) {
    jobsEvaluatorService.evaluateJobs(jobsInput);
    return jobsResultsCollectorService.collectJobsResults(jobsInput);
  }
}
