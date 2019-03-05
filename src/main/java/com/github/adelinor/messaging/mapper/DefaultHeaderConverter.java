package com.github.adelinor.messaging.mapper;

import java.util.Arrays;

/**
 * Generic implementation to be used by default for
 * conversion of header values.
 * By default values are represented as strings in header values.
 * The converter handles <strong>primitive types</strong>,
 * <strong>primitive type wrapper classes</strong> and
 * <strong>Enum</strong> classes.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 5 Mar 2019 (creation date)
 */
public class DefaultHeaderConverter implements HeaderConverter<Object, String> {

	/**
	 * List of primitive classes supported by this converter.
	 */
	public static final Class<?>[] SUPPORTED_PRIMITIVE_CLASSES = new Class<?>[] {
		Boolean.class, Character.class, Byte.class, Short.class,
		Integer.class, Long.class, Float.class, Double.class
	};
	
	@Override
	public String convertToHeaderValue(Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object convertToObjectValue(String value) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean canConvert(Class<?> objectValueType) {
		return objectValueType == String.class ||
				objectValueType.isEnum() ||
				(objectValueType.isPrimitive() && objectValueType != Void.TYPE) ||
				Arrays.stream(SUPPORTED_PRIMITIVE_CLASSES)
					.anyMatch( c -> objectValueType == c);
	}

}
