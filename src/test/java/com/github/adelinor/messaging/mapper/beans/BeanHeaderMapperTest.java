package com.github.adelinor.messaging.mapper.beans;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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
	void testFromHeaders_CustomConverter() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("receiveDate", 0L);
		headers.put("BATCH_NUMBER", "12345");
		
		BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
		MyMessage m = new MyMessage();
		mapper.fromHeaders(headers, m);
		
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		// Set 1970-01-01T00:00:00.000
		cal.set(1970, 0, 1, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		assertThat(m.getReceiveDate()).isEqualTo(cal.getTime());
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
	void testFromHeaders_NullPrimitive() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("IS_DUPLICATE", null);
		headers.put("BATCH_NUMBER", "12345");
		
		BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
		MyMessage m = new MyMessage();
		m.setDuplicate(true);
		mapper.fromHeaders(headers, m);
		
		assertThat(m.isDuplicate()).as("Null value is ignored").isTrue();
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
		assertThat(m.getFlags()).isEqualTo((byte) 3);
		assertThat(m.getRevisionNumber()).isEqualTo((short) 54);
		assertThat(m.getSequence()).isEqualTo(7);		
		assertThat(m.getIdentifier()).isEqualTo(10021L);		
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
	void testToHeaders_Null() {
		MyMessage m = new MyMessage();
		m.setMessageNumber("12345");
		m.setValid(null);
		
		Map<String, Object> headers = new HashMap<>();
		BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
		mapper.toHeaders(m, headers);
		assertThat(headers.containsKey("IS_VALID")).isFalse();
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

	@Test
	void testToHeaders_Primitive() {
		MyMessagePrimitives m = new MyMessagePrimitives();
		m.setDuplicate(true);
		m.setPriority('A');
		m.setFlags((byte) 3); 
		m.setSequence(7);
		m.setRevisionNumber((short) 54);
		m.setIdentifier(10021L);
		m.setRate(0.02f);
		m.setIncrease(10.202d);
		
		Map<String, Object> headers = new HashMap<>();
		BeanHeaderMapper<MyMessagePrimitives> mapper = new BeanHeaderMapper<>(MyMessagePrimitives.class);
		mapper.toHeaders(m, headers);

		for (String header : Arrays.asList("IS_DUPLICATE", "PRIORITY","FLAGS",
				"REVISION","SEQ","ID","RATE","INC")) {
			assertThat(headers.containsKey(header))
				.as("Found %s header", header)
				.isTrue();
		}

		assertThat(headers.get("IS_DUPLICATE")).isEqualTo("true");
		assertThat(headers.get("PRIORITY")).isEqualTo("A");
		assertThat(headers.get("FLAGS")).isEqualTo("3");
		assertThat(headers.get("REVISION")).isEqualTo("54");
		assertThat(headers.get("SEQ")).isEqualTo("7");
		assertThat(headers.get("ID")).isEqualTo("10021");
		assertThat(headers.get("RATE")).isEqualTo("0.02");
		assertThat(headers.get("INC")).isEqualTo("10.202");
	}

	@Test
	void testToHeaders_CustomConverter() {
		MyMessage m = new MyMessage();
		Date date = new Date();
		Long expectedMillis = 12345L;
		date.setTime(expectedMillis);
		m.setReceiveDate(date);
		
		m.setMessageNumber("12345");

		Map<String, Object> headers = new HashMap<>();
		
		BeanHeaderMapper<MyMessage> mapper = new BeanHeaderMapper<>(MyMessage.class);
		mapper.toHeaders(m, headers);
		
		assertThat(headers.get("receiveDate")).isEqualTo(expectedMillis);
	}

}
