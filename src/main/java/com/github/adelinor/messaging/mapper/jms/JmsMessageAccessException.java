package com.github.adelinor.messaging.mapper.jms;

import javax.jms.JMSException;

/**
 * Thrown when an error occurs while accessing methods of
 * a JMS message.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 11 Mar 2019 (creation date)
 */
public class JmsMessageAccessException extends RuntimeException {

	private static final long serialVersionUID = -216590667648419768L;

	/**
	 * @param message Descriptive error message
	 * @param exc Exception that occurred
	 */
	public JmsMessageAccessException(String message, JMSException exc) {
		super(message, exc);
	}
	
	/**
	 * @return The exception which triggered this
	 */
	public JMSException getJmsException() {
		return (JMSException) this.getCause();
	}
}
