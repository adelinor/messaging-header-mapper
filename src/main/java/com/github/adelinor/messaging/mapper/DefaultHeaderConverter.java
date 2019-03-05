package com.github.adelinor.messaging.mapper;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	private static class EnumHeaderConverter implements HeaderConverter<Enum<?>, String> {
		
		@SuppressWarnings("rawtypes")
		private Class enumType;
		
		@SuppressWarnings("rawtypes")
		private EnumHeaderConverter(Class enumType) {
			this.enumType = enumType;
		}

		@Override
		public String convertToHeaderValue(Enum<?> value) {
			return value.name();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Enum<?> convertToObjectValue(String value) {
			return Enum.valueOf(enumType, (String) value);
		}
	}

	private static class BooleanHeaderConverter implements HeaderConverter<Boolean, String> {

		@Override
		public String convertToHeaderValue(Boolean value) {
			return value.toString();
		}

		@Override
		public Boolean convertToObjectValue(String value) {
			return Boolean.valueOf(value);
		}
	}

	private static class CharacterHeaderConverter implements HeaderConverter<Character, String> {

		@Override
		public String convertToHeaderValue(Character value) {
			return value.toString();
		}

		@Override
		public Character convertToObjectValue(String value) {
			if (value.isEmpty()) {
				throw new IllegalArgumentException("An empty string cannot be converted to "
						+ "a Character instance");
			} else if (value.length() > 1) {
				throw new IllegalArgumentException("'" + value + "' cannot be converted to "
						+ "a Character instance");
			} else {
				return value.charAt(0);
			}
		}
	}

	/**
	 * Registry of converters for the getConverter method.
	 */
	private List<Map.Entry<Class<?>, HeaderConverter<?, ?>>> registry;
	
	/** Cannot be instantiated by outside callers */
	private DefaultHeaderConverter() {
		this.registry = new ArrayList<>();
		this.addConverter(new StringHeaderConverter(), String.class);
		this.addConverter(new BooleanHeaderConverter(), Boolean.class, Boolean.TYPE);
		this.addConverter(new CharacterHeaderConverter(), Character.class, Character.TYPE);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private synchronized void addConverter(HeaderConverter<?, String> converter, Class... forClasses) {
		for (Class forClass : forClasses) {
			this.registry.add(new AbstractMap.SimpleImmutableEntry(forClass, converter));			
		}
	}

	public static boolean canConvert(Class<?> objectValueType) {
		return objectValueType == String.class ||
				objectValueType.isEnum() ||
				(objectValueType.isPrimitive() && objectValueType != Void.TYPE) ||
				Arrays.stream(SUPPORTED_PRIMITIVE_CLASSES)
					.anyMatch( c -> objectValueType == c);
	}

	/**
	 * @param objectValueType Class of value in object
	 * @return Converter or null is the type is not supported
	 */
	@SuppressWarnings("unchecked")
	public static <T> HeaderConverter<T, String> getConverter(Class<T> objectValueType) {
		for (Entry<Class<?>, HeaderConverter<?, ?>> entry : INSTANCE.registry) {
			if (entry.getKey() == objectValueType) {
				return (HeaderConverter<T, String>) entry.getValue();
			}
		}
		
		// For enum types, converters are added on demand
		if (objectValueType.isEnum()) {
			EnumHeaderConverter result = new EnumHeaderConverter(objectValueType);
			INSTANCE.addConverter(result, objectValueType);
			return (HeaderConverter<T, String>) result;
		}
		return null;
	}

	/**
	 * This method is not supported to be consistent with {@link #convertToObjectValue(String)}.
	 */
	@Override
	public String convertToHeaderValue(Object value) {
		throw new UnsupportedOperationException("Get converter with getConverter method");
	}

	/**
	 * Unfortunately an implementation supporting multiple types
	 * cannot be done without knowing the intended target type.
	 */
	@Override
	public Object convertToObjectValue(String value) {
		throw new UnsupportedOperationException("Get converter with getConverter method");
	}

}
