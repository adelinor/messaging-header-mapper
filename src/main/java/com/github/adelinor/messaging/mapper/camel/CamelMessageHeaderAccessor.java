package com.github.adelinor.messaging.mapper.camel;

import org.apache.camel.Message;

import com.github.adelinor.messaging.mapper.HeaderAccessor;

/**
 * Accesses headers in a JMS Message.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 10 Mar 2019 (creation date)
 */
public class CamelMessageHeaderAccessor implements HeaderAccessor<Message> {

	/**
	 * As Camel message do not have an equivalent, this implementation
	 * gets the Map representation of the headers and checks the
	 * presence with that.
	 * See {@link Message#getHeaders()} implementation in DefaultMessage
	 * class.
	 */
	@Override
	public boolean has(Message message, String headerName) {
		return message.getHeaders().containsKey(headerName);
	}

	@Override
	public Object get(Message message, String headerName) {
		return message.getHeader(headerName);
	}

	@Override
	public void put(Message message, String headerName, Object value) {
		message.setHeader(headerName, value);
	}

}
