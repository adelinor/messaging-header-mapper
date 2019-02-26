package com.github.adelinor.messaging.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

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
		assertEquals("12345", m.getMessageNumber());
	}

	@Test
	void testFromHeaders_MissingRequired() {
		fail("Not yet implemented");
	}

	@Test
	void testToHeaders() {
		fail("Not yet implemented");
	}

}
