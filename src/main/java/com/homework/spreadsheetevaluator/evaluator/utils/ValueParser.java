package com.homework.spreadsheetevaluator.evaluator.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.homework.spreadsheetevaluator.evaluator.dto.ValueCell;
import com.homework.spreadsheetevaluator.evaluator.dto.enums.ValueCellType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.util.Assert;

public class ValueParser {
  public static final String VALUE_TYPE = "value";

  public static ValueCell parseValueCell(JsonNode jsonNode) {
    JsonNode valueNode = jsonNode.at("/" + VALUE_TYPE);
    Assert.isTrue(jsonNode.size() == 1, "Not single value node");
    List<ValueCellType> matchingTypes = Stream.of(ValueCellType.values())
        .filter(type -> valueNode.has(type.getProperty()))
        .collect(Collectors.toList());
    Assert.isTrue(matchingTypes.size() == 1, "Not single value type");
    ValueCellType type = matchingTypes.get(0);
    JsonNode extractedValueNode = valueNode.at(type.getPath());
    Assert.isTrue(type.getTypeValidator().apply(extractedValueNode), "Value doesn't match type");
    return new ValueCell(type, type.getValueParser().apply(extractedValueNode));
  }
}
