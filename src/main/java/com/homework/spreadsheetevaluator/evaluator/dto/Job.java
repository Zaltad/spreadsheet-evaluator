package com.homework.spreadsheetevaluator.evaluator.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.Value;

@Value
public class Job {
  String id;
  List<List<JsonNode>> data;
}
