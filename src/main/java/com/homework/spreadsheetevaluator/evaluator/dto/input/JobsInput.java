package com.homework.spreadsheetevaluator.evaluator.dto.input;

import com.homework.spreadsheetevaluator.evaluator.dto.Job;
import java.util.List;
import lombok.Value;

@Value
public class JobsInput {
  String submissionUrl;
  List<Job> jobs;
}
