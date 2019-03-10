package com.github.adelinor.messaging.mapper.beans;

import java.util.Map;

import com.github.adelinor.messaging.mapper.Header;
import com.github.adelinor.messaging.mapper.MapHeaderAccessor;
import com.github.adelinor.messaging.mapper.MapHeaderMapper;

/**
 * Uses the {@link Header} annotation to generating
 * the mapping between headers and a java object.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 26 Feb 2019 (creation date)
 */
public class BeanMapHeaderMapper<T> implements MapHeaderMapper<T> {

	/** Shared instance */
	private static final MapHeaderAccessor MAP_HEADER_ACCESSOR = new MapHeaderAccessor();
	
	/** Bean implementation to use */
	private BeanHeaderMapper<Map<String, Object>, T> beanMapper;
	
	public BeanMapHeaderMapper(Class<T> beanClass) {
		this.beanMapper = new BeanHeaderMapper<>(beanClass, MAP_HEADER_ACCESSOR);
	}

	@Override
	public void fromHeaders(Map<String, Object> headers, T target) {
		this.beanMapper.fromHeaders(headers, target);
	}

	@Override
	public void toHeaders(T source, Map<String, Object> headers) {
		this.beanMapper.toHeaders(source, headers);
	}
}
