package com.github.adelinor.messaging.mapper.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.adelinor.messaging.mapper.Header;
import com.github.adelinor.messaging.mapper.MapHeaderMapper;

/**
 * Uses the {@link Header} annotation to generating
 * the mapping between headers and a java object.
 *
 * @author  Adelino Rodrigues (created by)
 * @since 26 Feb 2019 (creation date)
 */
public class BeanHeaderMapper<T> implements MapHeaderMapper<T> {
		
	private List<MappingData> mappings;
	
	public BeanHeaderMapper(Class<T> beanClass) {
		IntrospectionMappingExtractor extractor = new IntrospectionMappingExtractor();

		this.mappings = Arrays.stream( beanClass.getDeclaredFields() )
			.map( f -> extractor.extractFor(beanClass, f) )
			.filter( m -> m != null)
			.collect(Collectors.toList());	
	}

	@Override
	public void fromHeaders(Map<String, Object> headers, T target) {
		for (MappingData mapping : mappings) {
			boolean hasHeader = headers.containsKey(mapping.getHeaderName());
			if ((! hasHeader) && mapping.isRequired()) {
				throw new IllegalArgumentException(mapping.getHeaderName() + 
						" is required and was not provided in " + headers);
			}
			if (hasHeader) {
				Object value = headers.get(mapping.getHeaderName());
				Object objectValue = null;
				
				if (value != null) {
					Class<?> valueClass = value.getClass();

					if (mapping.getFieldType().isAssignableFrom(valueClass)) {
						objectValue = value;

					} else if (mapping.getFieldType().isEnum() && valueClass == String.class) {
						objectValue = convertToEnumValue((String) value, mapping.getFieldType());

					} else if (mapping.getFieldType().isPrimitive() && valueClass == String.class) {
						objectValue = convertToPrimitiveValue((String) value, mapping.getFieldType());
					} else if (mapping.getFieldType() == Boolean.class && valueClass == String.class) {
						objectValue = convertToPrimitiveValue((String) value, Boolean.TYPE);
					}

					
				}
				try {
					mapping.getSetter().invoke(target, objectValue);
				} catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException exc) {
					throw new IllegalStateException("Cannot set property " +
							mapping.getFieldName() + " on object of type " +
							target.getClass(), exc);
				}
			}
		}
	}

	@Override
	public void toHeaders(T source, Map<String, Object> headers) {
		for (MappingData mapping : mappings) {
			Object value;
			try {
				value = mapping.getGetter().invoke(source);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exc) {
				throw new IllegalStateException("Cannot get property " +
						mapping.getFieldName() + " on object of type " +
						source.getClass(), exc);
			}
			
			if (value == null && mapping.isRequired()) {
				throw new IllegalArgumentException(mapping.getHeaderName() + 
						" is required and field " + mapping.getFieldName() +
						" is null");
			}
			if (value != null) {
				Object headerValue = null;
				Class<?> valueClass = value.getClass();
				if (mapping.getHeaderType().isAssignableFrom(valueClass)) {
					headerValue = value;
				} else if (mapping.getHeaderType() == String.class) {
					headerValue = value.toString();
				}
				headers.put(mapping.getHeaderName(), headerValue);
			}
		}
	}

	/**
	 * Converts a string value to an Enum.
	 * 
	 * @param value Value to convert
	 * @param enumType   Class of the enum corresponding to the value
	 * @return Instance of enum which corresponds to the value
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Enum<?> convertToEnumValue(String value, Class enumType) {
		return Enum.valueOf(enumType, (String) value);
	}
	
	private static Object convertToPrimitiveValue(String value, Class<?> primitiveType) {
		if (primitiveType == Boolean.TYPE) {
			return Boolean.parseBoolean(value);
		}
		// TODO
		return null;
	}

}
