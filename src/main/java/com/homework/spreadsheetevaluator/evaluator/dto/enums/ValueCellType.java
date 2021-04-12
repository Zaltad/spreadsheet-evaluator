package com.homework.spreadsheetevaluator.evaluator.dto.enums;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.function.Function;

public enum ValueCellType {
  NUMBER("number", JsonNode::doubleValue, JsonNode::isNumber),
  TEXT("text", JsonNode::textValue, JsonNode::isTextual),
  BOOLEAN("boolean", JsonNode::booleanValue, JsonNode::isBoolean);

  ValueCellType(String property, Function<JsonNode, Object> valueParser, Function<JsonNode, Boolean> typeValidator) {
    this.property = property;
    this.valueParser = valueParser;
    this.typeValidator = typeValidator;
  }

  private final String property;
  private final Function<JsonNode, Object> valueParser;
  private final Function<JsonNode, Boolean> typeValidator;

  public Function<JsonNode, Object> getValueParser() {
    return valueParser;
  }

  public Function<JsonNode, Boolean> getTypeValidator() {
    return typeValidator;
  }

  public String getProperty() {
    return property;
  }

  public String getPath() {
    return "/" + property;
  }
}
