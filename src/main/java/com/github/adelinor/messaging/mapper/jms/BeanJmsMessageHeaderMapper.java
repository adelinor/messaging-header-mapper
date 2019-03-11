package com.github.adelinor.messaging.mapper.jms;

import javax.jms.Message;

import com.github.adelinor.messaging.mapper.Header;
import com.github.adelinor.messaging.mapper.HeaderMapper;
import com.github.adelinor.messaging.mapper.beans.BeanHeaderMapper;

/**
 * Uses the {@link Header} annotation to generating
 * the mapping between headers and a java object.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 11 Mar 2019 (creation date)
 */
public class BeanJmsMessageHeaderMapper {

	/** Shared instance */
	private static final JmsMessageHeaderAccessor JMS_HEADER_ACCESSOR = new JmsMessageHeaderAccessor();
	
	/** Hide constructor */
	private BeanJmsMessageHeaderMapper() {}

	/** Create mapper instance for class */
	public static <T> HeaderMapper<Message, T> forClass(Class<T> beanClass) {
		return new BeanHeaderMapper<>(beanClass, JMS_HEADER_ACCESSOR);
	}
}
