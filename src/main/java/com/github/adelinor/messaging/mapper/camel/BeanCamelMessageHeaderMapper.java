package com.github.adelinor.messaging.mapper.camel;

import org.apache.camel.Message;

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
public class BeanCamelMessageHeaderMapper {

	/** Shared instance */
	private static final CamelMessageHeaderAccessor CAMEL_HEADER_ACCESSOR = new CamelMessageHeaderAccessor();
	
	/** Hide constructor */
	private BeanCamelMessageHeaderMapper() {}

	/** Create mapper instance for class */
	public static <T> HeaderMapper<Message, T> forClass(Class<T> beanClass) {
		return new BeanHeaderMapper<>(beanClass, CAMEL_HEADER_ACCESSOR);
	}
}
