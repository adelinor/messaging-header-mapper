package com.github.adelinor.messaging.mapper.beans;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.adelinor.messaging.mapper.Header;
import com.github.adelinor.messaging.mapper.HeaderConverter;
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
				
				if (value != null) {
					@SuppressWarnings("unchecked")
					HeaderConverter<Object, Object> converter = (HeaderConverter<Object, Object>) mapping.getConverter();
					Object objectValue = converter.convertToObjectValue(value);

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
				@SuppressWarnings("unchecked")
				HeaderConverter<Object, Object> converter = (HeaderConverter<Object, Object>) mapping.getConverter();
				Object headerValue = converter.convertToHeaderValue(value);
				headers.put(mapping.getHeaderName(), headerValue);
			}
		}
	}
}
