package com.github.adelinor.messaging.mapper.beans;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.github.adelinor.messaging.mapper.MapHeaderMapper;
import com.github.adelinor.sample.readwrite.KeyAttributes;

class BeanMapHeaderMapperReadWriteTest {
	
	private MapHeaderMapper<KeyAttributes> mapper = new BeanMapHeaderMapper<>(KeyAttributes.class);

	@Test
	void testFromHeaders_MissingRequired() {
		Map<String, Object> headers = new HashMap<>();
		
		KeyAttributes attrs = new KeyAttributes();
		assertThatIllegalArgumentException()
			.isThrownBy(() -> {
				mapper.fromHeaders(headers, attrs);
			})
			.withMessageContaining("encoding")
			.withNoCause();
	}

	@Test
	void testFromHeaders() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("encoding", "UTF-8");
		
		KeyAttributes attrs = new KeyAttributes();
		mapper.fromHeaders(headers, attrs);
		assertThat(attrs.hiddenGetEncoding())
			.isEqualTo("UTF-8");
	}

	@Test
	void testToHeaders_MissingRequired() {
		KeyAttributes attrs = new KeyAttributes();
		
		// Simulate a process that sets data but fails
		// to set a required output
		attrs.process(null, "MSG-123");
		
		Map<String, Object> headers = new HashMap<>();
		assertThatIllegalArgumentException()
			.isThrownBy(() -> {
				mapper.toHeaders(attrs, headers);
			})
			.withMessageContaining("sender")
			.withNoCause();
	}

	@Test
	void testToHeaders_Minimal() {
		KeyAttributes attrs = new KeyAttributes();
		
		// Simulate a process: the required output is set
		// the optional one remains blank
		attrs.process("ORG1", null);

		Map<String, Object> headers = new HashMap<>();
		mapper.toHeaders(attrs, headers);

		assertThat(headers.get("sender")).isEqualTo("ORG1");
		assertThat(headers).doesNotContainKey("messageNumber");
	}

	@Test
	void testToHeaders() {
		KeyAttributes attrs = new KeyAttributes();
		
		// Simulate a process: all output properties
		// are set
		attrs.process("ORG1", "MSG-123");

		Map<String, Object> headers = new HashMap<>();
		mapper.toHeaders(attrs, headers);

		assertThat(headers.get("sender")).isEqualTo("ORG1");
		assertThat(headers.get("messageNumber")).isEqualTo("MSG-123");
		assertThat(headers).hasSize(2);
	}

}
