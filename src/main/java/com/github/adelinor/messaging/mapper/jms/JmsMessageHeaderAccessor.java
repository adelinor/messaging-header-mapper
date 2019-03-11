package com.github.adelinor.messaging.mapper.jms;

import javax.jms.JMSException;
import javax.jms.Message;

import com.github.adelinor.messaging.mapper.HeaderAccessor;

/**
 * Accesses headers in a JMS Message.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 10 Mar 2019 (creation date)
 */
public class JmsMessageHeaderAccessor implements HeaderAccessor<Message> {

	@Override
	public boolean has(Message message, String headerName) {
		try {
			return message.propertyExists(headerName);

		} catch (JMSException exc) {
			throw new JmsMessageAccessException("Could not check presence"
					+ " of header " + headerName + " in message " + message, exc);
		}
	}

	@Override
	public Object get(Message message, String headerName) {
		try {
			return message.getObjectProperty(headerName);

		} catch (JMSException exc) {
			throw new JmsMessageAccessException("Could not retrieve value"
					+ " for header " + headerName + " in message " + message, exc);
		}
	}

	@Override
	public void put(Message message, String headerName, Object value) {
		try {
			message.setObjectProperty(headerName, value);

		} catch (JMSException exc) {
			throw new JmsMessageAccessException("Could not set value"
					+ " for header " + headerName + " in message " + message, exc);
		}
	}

}
