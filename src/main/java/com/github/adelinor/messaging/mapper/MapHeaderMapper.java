package com.github.adelinor.messaging.mapper;

import java.util.Map;

/**
 * Mapper between headers in the form of a Map
 * and a java bean.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 26 Feb 2019 (creation date)
 */
public interface MapHeaderMapper<T> {

	/**
	 * Maps values from message headers to java bean object.
	 * 
	 * @param headers Headers as a map
	 * @param target Target java bean object to populate
	 */
	void fromHeaders(Map<String, Object> headers, T target);
	
	/**
	 * Maps values from java bean object to message headers.
	 * 
	 * @param source Source of headers values as a java bean object
	 * @param headers Headers as a map to populate
	 */
	void toHeaders(T source, Map<String, Object> headers);

}
