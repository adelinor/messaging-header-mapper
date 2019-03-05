package com.github.adelinor.messaging.mapper.beans;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import com.github.adelinor.messaging.mapper.DefaultHeaderConverter;
import com.github.adelinor.messaging.mapper.Header;
import com.github.adelinor.messaging.mapper.HeaderConverter;

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

		HeaderConverter<?, ?> converter = getHeaderConverter(header.converter(), beanClass, field);
		PropertyDescriptor desc = getPropertyDescriptor(field.getName(), beanClass);
		String headerName = header.name();
		if (headerName.isEmpty()) {
			headerName = field.getName();
		}
		return new MappingData(
						field.getType(),
						field.getName(),
						headerName,
						converter,
						header.required(),
						desc.getReadMethod(),
						desc.getWriteMethod()
						);
	}

	private HeaderConverter<?, ?> getHeaderConverter(
			Class<? extends HeaderConverter<?, ?>> converterClass, 
			Class<?> beanClass, Field field) {
		if (converterClass == DefaultHeaderConverter.class) {
			if (! DefaultHeaderConverter.canConvert(field.getType())) {
				throw new IllegalArgumentException("Field " + field.getName() + " in class "
						+ beanClass + " needs to specify converter as type " + field.getType()
						+ " cannot be converted by default");
			}
			return DefaultHeaderConverter.getConverter(field.getType());
		}
		
		// Instantiate converter
		try {
			return converterClass.newInstance();

		} catch (InstantiationException exc) {
			throw new IllegalStateException("An error occurred while creating an instance of " +
					converterClass, exc);

		} catch (IllegalAccessException exc) {
			throw new IllegalArgumentException(converterClass + " needs to provide a public "
					+ "default constructor", exc);
		}
	}

	private PropertyDescriptor getPropertyDescriptor(String fieldName, Class<?> beanClass) {
		try {
			return new PropertyDescriptor(fieldName, beanClass);

		} catch (IntrospectionException exc) {
			throw new BeanHeaderMappingException(fieldName, beanClass, exc.getMessage());
		}
	}

}
