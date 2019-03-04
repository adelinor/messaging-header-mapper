package com.github.adelinor.messaging.mapper.beans;

import com.github.adelinor.messaging.mapper.Header;

/**
 * Thrown when an error occurred while extracting
 * the mapping information from a field annotated with
 * the {@link Header} annotation.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 4 Mar 2019 (creation date)
 */
public class BeanHeaderMappingException extends RuntimeException {

	private static final long serialVersionUID = 3300035240549154455L;

	public BeanHeaderMappingException(String fieldName, Class<?> beanClass, String message) {
		super("An error occurred while instrospecting " +
				fieldName + " from class " + beanClass.getName() + ": " +
				message);
	}
}
