package com.github.adelinor.messaging.mapper;

/**
 * Mapper between headers which format is represented by
 * the generic type H and a java bean class represented by
 * the generic type T.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 9 Mar 2019 (creation date)
 */
public interface HeaderMapper<H, T> {

	/**
	 * Maps values from message headers to java bean object.
	 * 
	 * @param headers Headers to read from
	 * @param target Target java bean object to populate
	 */
	void fromHeaders(H headers, T target);
	
	/**
	 * Maps values from java bean object to message headers.
	 * 
	 * @param source Source of headers values as a java bean object
	 * @param headers Headers to populate
	 */
	void toHeaders(T source, H headers);

}
