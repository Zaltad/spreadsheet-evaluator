package com.homework.spreadsheetevaluator.evaluator;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SubmissionConfig {
  private static final String HUB_URL = "https://www.wix.com/_serverless/hiring-task-spreadsheet-evaluator";

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.rootUri(HUB_URL).build();
  }
}
