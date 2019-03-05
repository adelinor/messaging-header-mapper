package com.github.adelinor.messaging.mapper;

/**
 * Similar to the JPA AttributeConverter, this defines
 * a strategy for converting a header value to an object
 * property value.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 5 Mar 2019 (creation date)
 */
public interface HeaderConverter<X, Y> {

	/**
	 * @param value Header value
	 * @return Converted value to assign to object
	 */
	Y convertToHeaderValue(X value);
	
	/**
	 * @param value Object value
	 * @return Converted value to assign to header
	 */
	X convertToObjectValue(Y value);
}
