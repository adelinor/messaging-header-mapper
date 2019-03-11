package com.github.adelinor.messaging.mapper.map;

import java.util.Map;

import com.github.adelinor.messaging.mapper.Header;
import com.github.adelinor.messaging.mapper.HeaderMapper;
import com.github.adelinor.messaging.mapper.beans.BeanHeaderMapper;

/**
 * Uses the {@link Header} annotation to generating
 * the mapping between headers and a java object.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 26 Feb 2019 (creation date)
 */
public class BeanMapHeaderMapper {

	/** Shared instance */
	private static final MapHeaderAccessor MAP_HEADER_ACCESSOR = new MapHeaderAccessor();
	
	/** Hide constructor */
	private BeanMapHeaderMapper() {}

	/** Create mapper instance for class */
	public static <T> HeaderMapper<Map<String, Object>, T> forClass(Class<T> beanClass) {
		return new BeanHeaderMapper<>(beanClass, MAP_HEADER_ACCESSOR);
	}
}
