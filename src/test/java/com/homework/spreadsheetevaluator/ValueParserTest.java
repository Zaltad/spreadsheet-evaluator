package com.homework.spreadsheetevaluator;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.spreadsheetevaluator.evaluator.dto.ValueCell;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.ValueCellType;
import com.homework.spreadsheetevaluator.evaluator.utils.ValueParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ValueParserTest {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void parseValueCell_shouldParseNumberType() throws JsonProcessingException {
    assertThat(ValueParser.parseValueCell(objectMapper.readTree("{ \"value\": { \"number\": 45.8 } }")))
        .isEqualTo(new ValueCell(ValueCellType.NUMBER, 45.8));
  }

  @Test
  void parseValueCell_shouldParseTextType() throws JsonProcessingException {
    assertThat(ValueParser.parseValueCell(objectMapper.readTree("{ \"value\": { \"text\": \"hire me!\" } }")))
        .isEqualTo(new ValueCell(ValueCellType.TEXT, "hire me!"));
  }

  @Test
  void parseValueCell_shouldParseBooleanType() throws JsonProcessingException {
    assertThat(ValueParser.parseValueCell(objectMapper.readTree("{ \"value\": { \"boolean\": true } }")))
        .isEqualTo(new ValueCell(ValueCellType.BOOLEAN, true));
  }
}
