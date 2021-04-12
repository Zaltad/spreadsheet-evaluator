package com.homework.spreadsheetevaluator;

import static org.assertj.core.api.Assertions.assertThat;

import com.homework.spreadsheetevaluator.evaluator.dto.Cell;
import com.homework.spreadsheetevaluator.evaluator.utils.ReferenceParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ReferenceParserTest {
  @Test
  void matchReference_shouldParseReference() {
    assertThat(ReferenceParser.matchReference("A2"))
        .isEqualTo(new Cell(1, 0));
  }
}
