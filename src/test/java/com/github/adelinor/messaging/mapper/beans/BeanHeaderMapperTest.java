package com.github.adelinor.messaging.mapper.beans;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.adelinor.messaging.mapper.beans.BeanHeaderMapper;
import com.github.adelinor.sample.MessageType;
import com.github.adelinor.sample.MyMessage;

class BeanHeaderMapperTest {

	@Test
	void testFromHeaders() {
		Map<String, Object> headers = new HashMap<>();
//		headers.put("MESSAGE_TYPE", "INBOUND");
		headers.put("BATCH_NUMBER", "12345");
		
		BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
		MyMessage m = new MyMessage();
		mapper.fromHeaders(headers, m);
		
//		assertEquals(MessageType.INBOUND, m.getMessageType());
		assertThat(m.getMessageNumber()).isEqualTo("12345");
	}

	@Test
	void testFromHeaders_MissingRequired() {
		Map<String, Object> headers = new HashMap<>();
		
		BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
		MyMessage m = new MyMessage();
		Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				mapper.fromHeaders(headers, m);
			})
			.withMessageContaining("BATCH_NUMBER")
			.withNoCause();
	}

	@Test
	void testToHeaders() {
		MyMessage m = new MyMessage();
		m.setMessageNumber("12345");
		
		Map<String, Object> headers = new HashMap<>();
		BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
		mapper.toHeaders(m, headers);
		assertThat(headers.get("BATCH_NUMBER")).isEqualTo("12345");
	}

	@Test
	void testToHeaders_MissingRequired() {
		MyMessage m = new MyMessage();
		Map<String, Object> headers = new HashMap<>();
		
		BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
		Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				mapper.toHeaders(m, headers);
			})
			.withMessageContaining("messageNumber is null")
			.withNoCause();
	}

}
