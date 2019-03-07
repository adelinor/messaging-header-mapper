package com.github.adelinor.messaging.mapper.beans;

import static java.util.Locale.ENGLISH;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import com.github.adelinor.messaging.mapper.DefaultHeaderConverter;
import com.github.adelinor.messaging.mapper.Header;
import com.github.adelinor.messaging.mapper.Header.Use;
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

		String headerName = header.name();
		if (headerName.isEmpty()) {
			headerName = field.getName();
		}

		HeaderConverter<?, ?> converter = getHeaderConverter(header.converter(), beanClass, field);

		PropertyDescriptor desc = getPropertyDescriptor(field, beanClass, header.use());

		return new MappingData(
						field.getType(),
						field.getName(),
						headerName,
						converter,
						header.required(),
						desc.getReadMethod(),
						desc.getWriteMethod(),
						header.use()
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

	private PropertyDescriptor getPropertyDescriptor(Field field,
			Class<?> beanClass, Use use) {
		String fieldName = field.getName();
		String setterName = null;
		if (use == Use.READONLY || use == Use.READWRITE) {
			setterName = "set" + capitalize(fieldName);
		}
		String getterName = null;
		if (use == Use.WRITEONLY || use == Use.READWRITE) {
			String prefix = (field.getType() == boolean.class) ? "is" : "get";
			getterName = prefix + capitalize(fieldName);
		}
		try {
			return new PropertyDescriptor(fieldName, beanClass, getterName, setterName);

		} catch (IntrospectionException exc) {
			throw new BeanHeaderMappingException(fieldName, beanClass, exc.getMessage());
		}
	}
	
	/**
	 * Copied from java.beans.NameGenarator.capitalize(String)
	 */
	private static String capitalize(String name) {
		return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
	}

}
