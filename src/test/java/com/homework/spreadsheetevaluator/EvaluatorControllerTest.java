package com.homework.spreadsheetevaluator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class EvaluatorControllerTest {
	private static Stream<Arguments> samples() {
		return Stream.of(
				Arguments.of("sample1.json", "result1.json"),
				Arguments.of("sample2.json", "result2.json"),
				Arguments.of("test1.json", "result3.json")
		);
	}

	@Autowired
	private MockMvc mockMvc;

	@ParameterizedTest
	@MethodSource("samples")
	void evaluate_shouldReturnCorrectResultForSample(String inputFileName, String outputFileName) throws Exception {
		var mvcResult = mockMvc.perform(post("/").contentType("application/json").content(IOUtils
				.toString(new ClassPathResource(inputFileName).getInputStream(), StandardCharsets.UTF_8)))
				.andExpect(status().isOk())
				.andReturn();
		assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(IOUtils
				.toString(new ClassPathResource(outputFileName).getInputStream(), StandardCharsets.UTF_8));
	}

}
