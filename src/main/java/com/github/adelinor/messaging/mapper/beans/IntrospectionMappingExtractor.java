package com.github.adelinor.messaging.mapper.beans;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import com.github.adelinor.messaging.mapper.Header;

/**
 * Extracts the mapping data from the Java Bean
 * introspection information.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 4 Mar 2019 (creation date)
 */
class IntrospectionMappingExtractor {

	/**
	 * Extracts mapping information.
	 * 
	 * @param beanClass Parent class of field
	 * @param field Field to map
	 * @return null if field is not annotated with Header or the
	 *         mapping information
	 */
	MappingData extractFor(Class<?> beanClass, Field field) {
		Header header = field.getAnnotation(Header.class);

		if (header == null) {
			return null;
		}

		PropertyDescriptor desc = getPropertyDescriptor(field.getName(), beanClass);
		return new MappingData(
						field.getType(),
						field.getName(),
						header.name(),
						header.required(),
						desc.getReadMethod(),
						desc.getWriteMethod()
						);
	}

	private PropertyDescriptor getPropertyDescriptor(String fieldName, Class<?> beanClass) {
		try {
			return new PropertyDescriptor(fieldName, beanClass);

		} catch (IntrospectionException exc) {
			throw new BeanHeaderMappingException(fieldName, beanClass, exc.getMessage());
		}
	}

}
