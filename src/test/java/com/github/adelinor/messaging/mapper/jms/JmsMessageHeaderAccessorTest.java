package com.github.adelinor.messaging.mapper.jms;

import static org.assertj.core.api.Assertions.assertThat;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JmsMessageHeaderAccessorTest {
	
	/** JMS connection to use */
	private Connection connection;

	/** JMS session to use */
	private Session session;

	/** Instance to test */
	private JmsMessageHeaderAccessor accessor = new JmsMessageHeaderAccessor();
	
	@BeforeEach
	void setUp() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
		this.connection = connectionFactory.createConnection();
		connection.start();

		this.session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
	}
	
	@AfterEach
	void tearDown() throws JMSException {
		session.close();
		connection.stop();
	}

	@Test
	void testHas() throws JMSException {
		Message message = session.createTextMessage();
		
		assertThat(accessor.has(message, "toto")).isFalse();
		assertThat(accessor.has(message, "titi")).isFalse();

		message.setObjectProperty("toto", null);
		assertThat(accessor.has(message, "toto")).isTrue();

		message.setIntProperty("titi", 123);
		assertThat(accessor.has(message, "titi")).isTrue();
	}

	@Test
	void testGet() throws JMSException {
		Message message = session.createTextMessage();

		assertThat(accessor.get(message, "toto")).isNull();
		assertThat(accessor.get(message, "titi")).isNull();
		assertThat(accessor.get(message, "tutu")).isNull();
		assertThat(accessor.get(message, "lili")).isNull();

		byte value = 3;
		message.setObjectProperty("toto", null);
		message.setByteProperty("titi", value);
		message.setBooleanProperty("tutu", true);
		message.setLongProperty("lili", 123L);
		assertThat(accessor.get(message, "toto")).isNull();
		assertThat(accessor.get(message, "titi"))
			.isInstanceOf(Byte.class)
			.isEqualTo(Byte.valueOf(value));
		assertThat(accessor.get(message, "tutu"))
			.isInstanceOf(Boolean.class)
			.isEqualTo(Boolean.TRUE);
		assertThat(accessor.get(message, "lili"))
			.isInstanceOf(Long.class)
			.isEqualTo(Long.valueOf(123L));
	}

	@Test
	void testPut() throws JMSException {
		Message message = session.createTextMessage();

		assertThat(accessor.has(message, "titi")).isFalse();

		String value = "123";
		accessor.put(message, "titi", value);
		assertThat(message.getObjectProperty("titi")).isSameAs(value);
		assertThat(accessor.get(message, "titi")).isSameAs(value);

		accessor.put(message, "titi", Boolean.TRUE);
		assertThat(message.getBooleanProperty("titi")).isTrue();
	}

}
