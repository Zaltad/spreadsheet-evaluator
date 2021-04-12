package com.homework.spreadsheetevaluator.evaluator.dto.response;

import com.homework.spreadsheetevaluator.evaluator.dto.Job;
import java.util.List;
import lombok.Value;

@Value
public class JobsResultsResponse {
  String email;
  List<Job> results;
}
