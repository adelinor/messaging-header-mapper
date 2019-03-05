package com.github.adelinor.messaging.mapper;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * Default implementation to be used for
 * conversion of header values.
 * 
 * By default values are represented as strings in header values.
 * 
 * This class acts as factory to create the converter instance for the
 * supported types: <strong>primitive types</strong>,
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
	
	/** Singleton instance */
	private static final DefaultHeaderConverter INSTANCE = new DefaultHeaderConverter();

	private static class StringHeaderConverter implements HeaderConverter<String, String> {

		@Override
		public String convertToHeaderValue(String value) {
			return value;
		}

		@Override
		public String convertToObjectValue(String value) {
			return value;
		}		
	}
	
	/**
	 * Registry of converters for the getConverter method.
	 */
	private List<Map.Entry<Class<?>, HeaderConverter<?, ?>>> registry;
	
	/** Cannot be instantiated by outside callers */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private DefaultHeaderConverter() {
		this.registry = new ArrayList<>();
		this.registry.add(new AbstractMap.SimpleImmutableEntry(
				String.class, new StringHeaderConverter()));
	}
		
	public static boolean canConvert(Class<?> objectValueType) {
		return objectValueType == String.class ||
				objectValueType.isEnum() ||
				(objectValueType.isPrimitive() && objectValueType != Void.TYPE) ||
				Arrays.stream(SUPPORTED_PRIMITIVE_CLASSES)
					.anyMatch( c -> objectValueType == c);
	}

	@SuppressWarnings("unchecked")
	public static <T> HeaderConverter<T, String> getConverter(Class<T> objectValueType) {
		for (Entry<Class<?>, HeaderConverter<?, ?>> entry : INSTANCE.registry) {
			if (entry.getKey() == objectValueType) {
				return (HeaderConverter<T, String>) entry.getValue();
			}
		}
		return null;
	}

	@Override
	public String convertToHeaderValue(Object value) {
		throw new UnsupportedOperationException("Get converter with getConverter method");
	}

	@Override
	public Object convertToObjectValue(String value) {
		throw new UnsupportedOperationException("Get converter with getConverter method");
	}

}
