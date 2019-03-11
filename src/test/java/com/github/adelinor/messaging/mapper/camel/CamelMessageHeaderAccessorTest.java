package com.github.adelinor.messaging.mapper.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CamelMessageHeaderAccessorTest extends CamelTestSupport {

	/** Test Exchange */
	private Exchange exchange;
	
	/** Instance to test */
	private CamelMessageHeaderAccessor accessor = new CamelMessageHeaderAccessor();

	@BeforeEach
	void camelTestSupportSetUp() throws Exception {
		super.setUp();
		this.exchange = new DefaultExchange(this.context);
	}

	@AfterEach
	void camelTestSupportTearDown() throws Exception {
		super.tearDown();
	}

	@Test
	void testHas() {
		Message message = this.exchange.getIn();

		Assertions.assertThat(accessor.has(message, "toto")).isFalse();
		Assertions.assertThat(accessor.has(message, "titi")).isFalse();

		message.setHeader("toto", null);
		Assertions.assertThat(accessor.has(message, "toto")).isTrue();

		message.setHeader("titi", 123);
		Assertions.assertThat(accessor.has(message, "titi")).isTrue();
	}

	@Test
	void testGet() {
		Message message = this.exchange.getIn();

		Assertions.assertThat(accessor.get(message, "toto")).isNull();
		Assertions.assertThat(accessor.get(message, "tutu")).isNull();
		Assertions.assertThat(accessor.get(message, "lili")).isNull();

		message.setHeader("toto", null);
		message.setHeader("tutu", true);
		message.setHeader("lili", 123L);
		Assertions.assertThat(accessor.get(message, "toto")).isNull();
		Assertions.assertThat(accessor.get(message, "tutu"))
			.isInstanceOf(Boolean.class)
			.isEqualTo(Boolean.TRUE);
		Assertions.assertThat(accessor.get(message, "lili"))
			.isInstanceOf(Long.class)
			.isEqualTo(Long.valueOf(123L));
	}

	@Test
	void testPut() {
		Message message = this.exchange.getIn();

		Assertions.assertThat(accessor.has(message, "titi")).isFalse();

		String value = "123";
		accessor.put(message, "titi", value);
		Assertions.assertThat(message.getHeader("titi")).isSameAs(value);
		Assertions.assertThat(accessor.get(message, "titi")).isSameAs(value);

		accessor.put(message, "titi", Boolean.TRUE);
		Assertions.assertThat(message.getHeader("titi")).isEqualTo(Boolean.TRUE);
	}

}
