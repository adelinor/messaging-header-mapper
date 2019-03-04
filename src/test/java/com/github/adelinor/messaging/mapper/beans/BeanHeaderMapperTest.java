package com.github.adelinor.messaging.mapper.beans;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.adelinor.messaging.mapper.beans.BeanHeaderMapper;
import com.github.adelinor.sample.MessageType;
import com.github.adelinor.sample.MyMessage;
import com.github.adelinor.sample.primitives.MyMessagePrimitives;

class BeanHeaderMapperTest {

	@Test
	void testFromHeaders() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("MESSAGE_TYPE", "INBOUND");
		headers.put("BATCH_NUMBER", "12345");
		
		BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
		MyMessage m = new MyMessage();
		mapper.fromHeaders(headers, m);
		
		assertThat(m.getMessageType()).isEqualTo(MessageType.INBOUND);
		assertThat(m.getMessageNumber()).isEqualTo("12345");
		assertThat(m.getValid()).isNull();
		assertThat(m.isDuplicate()).isFalse();
	}

	@Test
	void testFromHeaders_Boolean() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("IS_VALID", "true");
		headers.put("BATCH_NUMBER", "12345");
		
		BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
		MyMessage m = new MyMessage();
		mapper.fromHeaders(headers, m);
		
		assertThat(m.getValid()).isEqualTo(Boolean.TRUE);
	}

	@Test
	void testFromHeaders_BooleanPrimitive() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("IS_DUPLICATE", "true");
		headers.put("BATCH_NUMBER", "12345");
		
		BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
		MyMessage m = new MyMessage();
		mapper.fromHeaders(headers, m);
		
		assertThat(m.isDuplicate()).isTrue();
	}

	@Test
	void testFromHeaders_Primitive() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("IS_DUPLICATE", "true");
		headers.put("PRIORITY", "A");
		headers.put("FLAGS", "3");
		headers.put("REVISION", "54");
		headers.put("SEQ", "7");
		headers.put("ID", "10021");
		headers.put("RATE", "0.02");
		headers.put("INC", "10.202");
		
		BeanHeaderMapper<MyMessagePrimitives> mapper = new BeanHeaderMapper<>(MyMessagePrimitives.class);
		MyMessagePrimitives m = new MyMessagePrimitives();
		mapper.fromHeaders(headers, m);
		
		assertThat(m.isDuplicate()).isTrue();
		assertThat(m.getPriority()).isEqualTo('A');
		assertThat(m.getFlags()).isEqualTo(3);
		assertThat(m.getRevisionNumber()).isEqualTo(54);
		assertThat(m.getSequence()).isEqualTo(7);		
		assertThat(m.getIdentifier()).isEqualTo(10021);		
		assertThat(m.getRate()).isEqualTo(0.02f);		
		assertThat(m.getIncrease()).isEqualTo(10.202d);		
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
		m.setMessageType(MessageType.INBOUND);
		
		Map<String, Object> headers = new HashMap<>();
		BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
		mapper.toHeaders(m, headers);
		assertThat(headers.get("BATCH_NUMBER")).isEqualTo("12345");
		assertThat(headers.get("MESSAGE_TYPE")).isEqualTo("INBOUND");
		assertThat(headers.containsKey("IS_VALID")).isFalse();
		assertThat(headers.get("IS_DUPLICATE")).isEqualTo("false");
	}

	@Test
	void testToHeaders_Boolean() {
		MyMessage m = new MyMessage();
		m.setMessageNumber("12345");
		m.setDuplicate(true);
		m.setValid(Boolean.FALSE);
		
		Map<String, Object> headers = new HashMap<>();
		BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
		mapper.toHeaders(m, headers);
		assertThat(headers.get("IS_VALID")).isEqualTo("false");
		assertThat(headers.get("IS_DUPLICATE")).isEqualTo("true");
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
